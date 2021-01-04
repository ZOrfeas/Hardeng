/*const axios = require("axios");
const yargs = require("yargs");
//const argv = yargs
var argv = require('yargs/yargs')(process.argv.slice(2))
.command('Admin', 'System Administration')
.option
  ('usermod',
  {
    description: 'Create new user or change password'
  })
.option('users',
  {
    description: "Show user's situation"
  })
.option('sessionsupd',
  {
    description: "Add new sessions from csv file"
  })
.option('healthcheck',
  {
    description: ''
  })
.option('resetsessions',
  {
    description: '' 
  })

.option('passw',{
  description: "User's password",
  type: 'string'
})
.option('users',{
  description: "Show user's activity"
})
.option('source',{
  description: 'A csv file that contains an archive of sessions',
  type: 'file'
})
.demandOption(['usermod', 'username', 'passw'])
.demandOption(['username', 'passw'])
.demandOption(['sessionsupd', 'source'])
.help()
.alias('help', 'h')
.argv;*/