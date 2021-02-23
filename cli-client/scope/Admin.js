const axios = require("axios");
const { builder } = require("./SessionsPerProvider");

exports.command = 'Admin'

exports.desc = "System Administration"

exports.builder = {
  usermod:
  {
    describe: "Create user or change user's password\n'--usermod --username <User's name> --passw <Password>'",
    
  },
  username: {
    describe: "User's name"
  },
  passw: {
    describe: "User's password"
  },
  users:
  {
    describe: "User's Activity"
  },
  sessionupd:
  {
    describe: "Add new sessions from a csv file\n --sessionupd --source <Filename.csv>"
  },
  source: {
    describe: "File to upload"
  },
  healthcheck:
  {
    describe: "Check user-database connection"
  },
  resetsessions:
  {
    describe: 'Initialize charging events & default admin account'

  }
}

exports.handler = function(argv)
{
  var myArgs = process.argv.slice(3)
  if(myArgs[0] == '--usermod'){
    if(myArgs[1] != '--username' || myArgs[3] != '--passw')//if myArgs[2] or myArgs[4] == 0 API will return error 400
    {
      console.log('Please make sure to enter username and password')
      console.log("For more, type: Admin --help")
    }
    else{
      console.log('APIkey: ', myArgs[8])
    }
  }
  if(myArgs[0] == '--users'){
    console.log("Username: ", 'API key')
  }
  if(myArgs[0] == '--sessionupd')
  {
    if(myArgs[1] != '--source')
    {
      console.log("Please add the csv file with sessions you want to upload\nFor more, type: Admin --help")
    }
    else{
      console.log(myArgs[2])
      
    }
  }
  if(myArgs[0] == '--healthcheck')
  {
    axios.get('/admin/healthcheck', {
      params: {
          apikey: argv.apikey
      }
  })
    .then(res =>{
      console.log(res.data);
  })
  .catch(err => {
      console.log(err.response.data)
  })
  }
  if(myArgs[0] == '--resetsessions')
  {
    axios.post('/admin/resetsessions', {
      params: {
          apikey: argv.apikey
      }
  })
    .then(res =>{
      console.log(res.data);
  })
  .catch(err => {
      console.log(err.response.data)
  })
  }
}