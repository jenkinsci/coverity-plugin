import sys
import subprocess
import re 
import json




if __name__ == "__main__":
	# Check to make sure we have the correct number of agruments
	if len(sys.argv) != 3:
		print "Incorrect number of arugments given. Build.py takes two arguments, First is the build number $BUILD_NUMBER and second is build id $BUILD_ID"
		sys.exit(-1)
	# Save build number and id that was passed in from jenkins
	build_number = sys.argv[1]
	build_id = sys.argv[2]
	# Grap git commit id
	output = subprocess.Popen("git log --name-status HEAD^..HEAD | grep \"commit*\"", stdout=subprocess.PIPE, shell=True)
	commit = output.stdout.read()
	# Remove all head information, so that only the commit id is left
	commit_id = re.sub(r'\(.*?\)','',commit)
	commit_id = re.sub("commit","",commit_id)
	# Generate the json output text
	#json_output = "{" + "\n commit_id:" + commit_id.strip()+ "\n build_number: " + build_number + "\n build_id: " + build_id  + "\n}" 
	json_output = json.dumps({ "commit_id" : commit_id.strip(), "build_number" : build_number, "build_id" : build_id }, indent=4)
	# Run the typical build for jenkins
	subprocess.check_call("mvn clean install", shell=True)
	version_file = open("./target/coverity.hpi.VERSION","w")
	version_file.write(json_output)

