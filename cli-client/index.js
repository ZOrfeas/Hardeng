#!/usr/bin/env node

require('dotenv').config();

const https = require('https');
const axios = require("axios");

axios.defaults.baseURL = process.env.BASE_URL;

const httpsAgent = new https.Agent({
  rejectUnauthorized: false
})

axios.defaults.httpsAgent = httpsAgent;

require('yargs/yargs')(process.argv.slice(2))
  .commandDir('scope')
  .demandCommand(1)
  .strict()
  .help()
  .argv