const axios = require('axios')
const { ChartJSNodeCanvas } = require('chartjs-node-canvas');
const { promises } = require('fs');

const url = 'http://localhost:8080/students'
const qps = 5.0 // queries per second
const request_interval = 1000.0 / qps
const log_time = 10.0 // seconds
const limit_min = 1
const limit_max = 1000

let success_count = 0
let failure_count = 0
let total_time_elapsed = 0
let request_count = 0
let request_id = 0

let request_info = {
    "success": {},
    "failure": {},
}

function sendRequest() {
    const startTime = Date.now()
    const limit = Math.floor(Math.random() * (limit_max - limit_min + 1) + limit_min)
    axios.get(url, { params: { limit: limit } })
        .then(response => {
            const endTime = Date.now()
            const duration = Math.floor(endTime - startTime)
            if (response.data.code !== 200) {
                failure_count++
                console.log(`Request #${request_id++} failure: ${duration}`)
                total_time_elapsed += endTime - startTime
                if (duration in request_info["failure"]) {
                    request_info["failure"][duration]++
                } else {
                    request_info["failure"][duration] = 1
                }
            } else {
                success_count++
                console.log(`Request #${request_id++} success: ${duration} ms`)
                total_time_elapsed += endTime - startTime
                if (duration in request_info["success"]) {
                    request_info["success"][duration]++
                } else {
                    request_info["success"][duration] = 1
                }
            }
        })
        .catch(error => {
            failure_count++
            console.log(`Request #${request_id++} failure: ${error.message}`)
        })
}

async function visualizeStats() {
    const data = []
    for (let i = 1; i <= 60; i++) {
        const successCount = request_info["success"][i] || 0
        const failureCount = request_info["failure"][i] || 0
        data.push(successCount + failureCount)
    }

	const width = 400;
	const height = 400;
	const configuration = {
		type: 'bar',
		data: {
			labels: Array.from({ length: 60 }, (_, i) => `${i}`),
			datasets: [{
				label: 'X-axis: 响应时间 (ms), Y-axis: 对应请求次数',
				data: data,
				backgroundColor: [
					'rgba(255, 99, 132, 0.2)',
					'rgba(54, 162, 235, 0.2)',
					'rgba(255, 206, 86, 0.2)',
					'rgba(75, 192, 192, 0.2)',
					'rgba(153, 102, 255, 0.2)',
					'rgba(255, 159, 64, 0.2)'
				],
				borderColor: [
					'rgba(255,99,132,1)',
					'rgba(54, 162, 235, 1)',
					'rgba(255, 206, 86, 1)',
					'rgba(75, 192, 192, 1)',
					'rgba(153, 102, 255, 1)',
					'rgba(255, 159, 64, 1)'
				],
				borderWidth: 1
			}]
		},
		options: {
		},
		plugins: [{
			id: 'background-colour',
			beforeDraw: (chart) => {
				const ctx = chart.ctx;
				ctx.save();
				ctx.fillStyle = 'white';
				ctx.fillRect(0, 0, width, height);
				ctx.restore();
			}
		}]
	};
	const chartCallback = (ChartJS) => {
		ChartJS.defaults.responsive = true;
		ChartJS.defaults.maintainAspectRatio = false;
	};
	const chartJSNodeCanvas = new ChartJSNodeCanvas({ width, height, chartCallback });
	const buffer = await chartJSNodeCanvas.renderToBuffer(configuration);
	await promises.writeFile('./image.png', buffer, 'base64');
}


function printStats() {
    console.log(" === Stress Test Results === ")
    console.log(`QPS: ${qps}`)
    console.log(`Limit: ${limit_min}~${limit_max}`)
    console.log(`Log Time: ${log_time}`)
    console.log(`Success Times: ${success_count}`)
    console.log(`Failure Times: ${failure_count}`)
    console.log(`Total time elapsed: ${total_time_elapsed} ms`)
    console.log(`Average time elapsed: ${total_time_elapsed / request_count} ms`)
    console.log(``)
    visualizeStats();
}

console.log(`Starting stress test to ${url} ...`)
const interval_id = setInterval(() => {
    request_count += 1
    sendRequest()
    if (request_count * request_interval >= log_time * 1000) {
        clearInterval(interval_id)
        setTimeout(printStats, 3000) // ensure all requests are finished
    }
}, request_interval)
