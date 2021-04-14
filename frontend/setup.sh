IP_VAR="$(curl http://checkip.amazonaws.com/)"
CONFIG='{"ip": "'"$IP_VAR"'"}'
echo $CONFIG > src/ip.json

rm -rf ~/node_modules
rm -rf node_modules
rm -rf ~/.npm 
rm package-lock.json
mkdir -p ~/node_modules
npm install
npm start