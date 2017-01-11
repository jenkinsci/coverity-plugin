#/*******************************************************************************
# * Copyright (c) 2016 Synopsys, Inc
# * All rights reserved. This program and the accompanying materials
# * are made available under the terms of the Eclipse Public License v1.0
# * which accompanies this distribution, and is available at
# * http://www.eclipse.org/legal/epl-v10.html
# *
# * Contributors:
# *    Synopsys, Inc - initial implementation and documentation
# *******************************************************************************/
import sys
import subprocess
import re 
import json
import shutil

if __name__ == "__main__":
	# Check to make sure we have the correct number of arguments
	if len(sys.argv) != 4:
		print "Incorrect number of arguments given. Build.py takes three arguments, first is the version number, second is the build number $BUILD_NUMBER and third is build id $BUILD_ID"
		sys.exit(-1)
	# Save version, build number and id that was passed in from jenkins
	version = sys.argv[1]
	build_number = sys.argv[2]
	build_id = sys.argv[3]
	# git log for the current commit id hash
	output = subprocess.Popen("git log --pretty=format:'%H' -n 1", stdout=subprocess.PIPE, shell=True)
	commit_id = output.stdout.read()
	# Generate the json output text
	json_output = json.dumps({ "commit_id" : commit_id.strip(), "build_number" : build_number, "build_id" : build_id, "external_version" : version }, indent=4)
	# Run the typical build for jenkins
	subprocess.check_call("mvn clean install", shell=True)
	# write the version output file
	version_file = open("./target/coverity.hpi.VERSION","w")
	version_file.write(json_output)
	# move the .hpi file to a versioned file
	shutil.move("./target/coverity.hpi", "./target/coverity-{0}.hpi".format(version))

