const axios = require("axios");
const { builder } = require("./SessionsPerProvider");
const qs = require('querystring');
const FormData = require('form-data');
const fs = require('fs');
const { tokenFileExists, createTokenFile, errorHandler, getToken } = require("../utils");
const { help } = require("yargs");

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
  driverName:{
    describe: "User's name"
  },
  users:
  {
    describe: "User's Activity"
  },
  sessionsupd:
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
  var args = process.argv.slice(3);
  //console.log(args[0]);
  const token = getToken();
  switch(args[0])
  {
    case '--usermod':
      if(args[1] != '--username' || args[3] != '--passw' || args[5] != '--email' || args[7] != '--driverName')
      {
        console.log('Wrong input, please type Admin --help')
      }
      else
      {
        axios.post('/admin/usermod/'+argv.username+'/'+argv.passw,{
          
            driverName: argv.driverName,
            email: argv.email
          },{
          headers: {
            'X-OBSERVATORY-AUTH': token.toString()
          }    
          
        })
        
        .then(res => {
          console.log(res.data);
          console.log(token);
        })
        .catch(err => {
          errorHandler(err);
        })
      }
      break;

    case '--users':
      axios.get('/admin/users/'+argv.users,{
        headers:{
          'X-OBSERVATORY-AUTH': token.toString()
        }
      })
      .then(res => {
        console.log(res.data);
      })
      .catch(err => {
        errorHandler(err);
      })
      break;

    case '--sessionsupd':
      const form = new FormData();
      form.append('file',  fs.createReadStream(argv.source));
      axios.post('/admin/system/sessionsupd', form,      
      {
        headers:{
          'X-OBSERVATORY-AUTH': token.toString(),
          'Content-Type': `multipart/form-data; boundary=${form._boundary}`
        }
      })
      .then(res => {
        console.log(res.data);
      })
      .catch(err => {
        errorHandler(err);
      })
      break;

    case '--healthcheck':
      axios.get('/admin/healthcheck',{
        headers: {
          'X-OBSERVATORY-AUTH': token.toString()
      }
      })
      .then(res => {
          console.log(res.data);
      })
      .catch(err => {
          errorHandler(err);
      })
      break;

    case '--resetsessions':
      axios.post('/admin/resetsessions', {
        headers: {
            'X-OBSERVATORY-AUTH': token.toString()
        }
      })
      .then(res => {
          console.log(res.data);
      })
      .catch(err => {
          errorHandler(err);
      })
      break;

    default: 
    const helpMessage = `index.js Admin
    \r
    \rSystem Administration
    \r
    \rOptions:
    \r  --version   Show version number                                      [boolean]
    \r  --help      Show help                                                [boolean]
    \r  --usermod        Create user or change user's password
    \r                     '--usermod --username <username> --passw <Password>
    \r                      --email<Email> --name<User's name>'
    \r  --username       Username
    \r  --passw          Password
    \r  --email          User's email
    \r  --driverName     User's name
    \r  --users          User's Activity
    \r  --sessionsupd    Add new sessions from a csv file
    \r                    --sessionupd --source <Filename.csv>
    \r  --source         File to upload
    \r  --healthcheck    Check user-database connection
    \r  --resetsessions  Initialize charging events & default admin account`;
    console.log(helpMessage);
  }

}