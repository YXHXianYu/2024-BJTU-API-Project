const axios = require('axios')

const url = 'http://localhost:8080/students'
const qps = 100.0 // queries per second
const request_interval = 1000.0 / qps
const log_time = 20.0 // seconds
const limit = 1000

let success_count = 0
let failure_count = 0
let total_time_elapsed = 0
let request_count = 0
let request_id = 0

function sendRequest() {
    const startTime = Date.now()
    axios.get(url, { param: { limit: limit } })
        .then(response => {
            const endTime = Date.now()
            success_count++
            console.log(`Request #${request_id++} success: ${endTime - startTime} ms`)
            total_time_elapsed += endTime - startTime
        })
        .catch(error => {
            failure_count++
            console.log(`Request #${request_id++} failure: ${error.message}`)
        })
}

function printStats() {
    console.log(" === Stress Test Results === ")
    console.log(`QPS: ${qps}`)
    console.log(`Limit: ${limit}`)
    console.log(`Log Time: ${log_time}`)
    console.log(`Success Times: ${success_count}`)
    console.log(`Failure Times: ${failure_count}`)
    console.log(`Total time elapsed: ${total_time_elapsed} ms`)
    console.log(`Average time elapsed: ${total_time_elapsed / request_count} ms`)
    console.log(``)
}

console.log(`Starting stress test to ${url} ...`)
const interval_id = setInterval(() => {
    request_count += 1
    sendRequest()
    if (request_count * request_interval >= log_time * 1000) {
        clearInterval(interval_id)
        setTimeout(printStats, 10000) // ensure all requests are finished
    }
}, request_interval)
