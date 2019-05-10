let database = require('./db');
let worker = require('./worker');
const app = require('express')();
const bodyParser = require("body-parser");

app.use(bodyParser.json());
app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
  });  



app.post('/login', (req, res) => {

    database.getLoginData(req.body.name, req.body.password, queryResult =>{
        queryResult = JSON.parse(queryResult);
        if(queryResult.length != 0 && queryResult != undefined && queryResult != null)
        {
          database.getLastThreeRecords(queryResult[0].idc, queryResult =>{
            worker.checkLoginHistory(JSON.parse(queryResult), result =>{
              if(result)
              {
                return res.status(401).send();
              }
              else
              {
                return res.status(200).send();
              }
            });
          });
        }
        else{return res.status(401).send();}
    });
  });

app.listen(3125, () =>
  console.log('Server started on port 3125'),
);