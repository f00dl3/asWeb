#asWeb Auto Deployer
#Created: Dec 2019
#Updated: 8 Feb 2020

fn_ymd=$(date +%y%m%d)
fn_hm=$(date +%H%M)
ramWar="/dev/shm/asWeb/WEB-INF"
version=$fn_ymd"."$fn_hm
fileName="asWeb##"$version".war"
sqlStatement="INSERT INTO Core.WebVersion (Version, Date, Changes) VALUES ('"$version"',CURDATE(),'"$1"');"
publicIp=$(cat src/asWebRest/secure/JunkyPrivate.java | grep "publicIp" | grep -o '".*"' | sed 's/"//g')
helperSet="java -cp $ramWar/classes:$ramWar/lib/* asUtilsPorts.Shares.HelperPermissions"

echo "INFO: Extracted public IP is "$publicIp

echo "put '"$fileName"'" > tbf
echo "put '"$fileName"' pi2Scripts/asWeb.war" > tbg
echo "put '"$fileName"' piScripts/asWeb.war" > tbh

echo "File to generate: "$fileName
echo "SQL: "$sqlStatement

rm asWeb*.war
ant war > autoWar.log 2>&1
rm -fr bin
mv asWeb.war $fileName

if [[ -f "$fileName" ]]; then 
	ls -l $fileName
	echo "Copying to /Scripts on HOST OS"
	cp $fileName /home/astump/Scripts/asWeb.war
	echo "Local sudo auth to extract to RAM"
	sudo unzip /home/astump/Scripts/asWeb.war 'WEB-INF/*' -d /dev/shm/asWeb
	echo "Setting permissions for helpers & Boot Bundle in RAM"
	sudo $helperSet
	echo "MySQL Auth for DB log:"
	echo "$sqlStatement" | mysql -u f00dl3 -p -h 127.0.0.1
	echo "Deploying to Raspberry Pi #1 (background)..."
	sftp -i ~/.ssh/Desktop2Pi -P 39408 -b tbh pi@192.168.1.8 &
	echo "Deploying to Raspberry Pi #2 (background)..."
	sftp -i ~/.ssh/forPi2v2 -P 39409 -b tbg pi@$publicIp &
	echo "Deploying to Ubuntu Virtual Machine (Guest)..."
	sftp -i ~/.ssh/uvmKey -P 20022 -b tbf astump@127.0.0.1 
	echo "UVM sudo Auth:"
	ssh -t -i ~/.ssh/uvmKey -p 20022 astump@127.0.0.1 "sudo cp asWeb##*.war Scripts/asWeb.war; sudo mv asWeb#*.war /var/lib/tomcat9/webapps; sudo service tomcat9 restart"
else
	echo "BUILD FAILED!"
fi

cat autoWar.log | more

rm autoWar.log
rm tbf
rm tbg
rm tbh
