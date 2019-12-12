fn_ymd=$(date +%y%m%d)
fn_hm=$(date +%H%M)
version=$fn_ymd"."$fn_hm
fileName="asWeb##"$version".war"
sqlStatement="INSERT INTO Core.WebVersion (Version, Date, Changes) VALUES ('"$version"',CURDATE(),'"$1"');"
echo "put '"$fileName"'" > tbf

echo "File to generate: "$fileName
echo "SQL: "$sqlStatement

rm asWeb*.war
ant war
rm -fr bin
mv asWeb.war $fileName

if [[ -f "$fileName" ]]; then 
	ls -l $fileName
	echo "MySQL Auth:"
	echo "$sqlStatement" | mysql -u f00dl3 -p -h 127.0.0.1
	sftp -i ~/.ssh/uvmKey -P 20022 -b tbf astump@127.0.0.1
	echo "UVM sudo Auth:"
	ssh -t -i ~/.ssh/uvmKey -p 20022 astump@127.0.0.1 "sudo mv asWeb#*.war /var/lib/tomcat9/webapps; sudo service tomcat9 restart"
else
	echo "BUILD FAILED!"
fi

rm tbf
