import os
import subprocess
import shlex
import shutil

# Replace the directory, output_directory, and proto_file with your own values
directory = "./"
output_directory_java = "../../backend/src/main/java/"
output_directory_js = "../../frontend/src/proto/"

for root, dirs, files in os.walk(directory):
    for file in files:
        if file.endswith(".proto"):
            proto_path = os.path.join(root, file)

            command = f"protoc --java_out={output_directory_java} {proto_path}"
            subprocess.run(command, shell=True)
            print(f"Generated {proto_path} to {output_directory_java}")

            # command = f"protoc --js_out=import_style=es6,binary:{output_directory_js} {proto_path}"
            # subprocess.run(command, shell=True)
            # print(f"Generated {proto_path} to {output_directory_js}")

            shutil.copy(proto_path, output_directory_js)
            print(f"Copied {proto_path} to {output_directory_js}")

