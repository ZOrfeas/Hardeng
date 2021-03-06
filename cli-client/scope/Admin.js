const axios = require("axios");
const { builder } = require("./SessionsPerProvider");
const qs = require('querystring');
const { tokenFileExists, createTokenFile, errorHandler, getToken } = require("../utils");

exports.command = 'Admin'

exports.desc = "System Administration"

exports.builder = {
  usermod:
  {
    describe: "Create user or change user's password\n'--usermod --username <username> --passw <Password> --email<Email> --name<User's name>'",
    
  },
  username: {
    describe: "Username"
  },
  passw: {
    describe: "Password"
  },
  email: {
    describe: "User's email"
      
  },
  name:{
    describe: "User's name"
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
  var args = process.argv.slice(3)
  if(args[0] == '--usermod'){
    if(args[1] != '--username' || args[3] != '--passw' || args[5] != '--email' || args[7] !='--name')
    {
      console.log('Type Admin --help')
    }
    else{
      axios.post('admin/usermod/'+ args[2]+'/'+args[4], qs.stringify({
        username: args[2],
        password: args[4]
      }),
      {
        data:{
          email: args[6], 
          driverName: args[8]
        }
      })
      .then(res =>{
      console.log(res.data.getToken);
      })
      .catch(err => {
      console.log(err.response.data)
     })
    }
  }
}
