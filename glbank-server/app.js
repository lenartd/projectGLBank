let database = require('./db');
let worker = require('./worker');
const app = require('express')();
const bodyParser = require("body-parser");
let cors = require("cors");

app.use(cors());
app.use(bodyParser.json());
app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
  });  

let tokenArray = [];

app.post('/login', (req, res) => {

  database.getLoginByUsername(req.body.name, queryResult1 =>{
     queryResult1 = JSON.parse(queryResult1);
      if(queryResult1.length != 0 && queryResult1 != undefined && queryResult1 != null)
      {
        database.getLoginData(req.body.name, req.body.password, queryResult2 =>{
          queryResult2 = JSON.parse(queryResult2);
          if(queryResult2.length != 0 && queryResult2 != undefined && queryResult2 != null)
          {
            database.getLastThreeRecords(queryResult2[0].idc, queryResult =>{
              worker.checkLoginHistory(JSON.parse(queryResult), result =>{
                if(result)
                {
                  console.log("LOGIN HISTORY WRONG");
                  return res.status(401).send();
                }
                else
                {
                  worker.checkIfLoggedUser(req.body.name, tokenArray, result=>{
                    if(result > -1)
                    {
                      worker.verifyUserToken(req, tokenArray, verified =>{
                        if(verified > -1)
                        {
                          tokenArray.splice(verified, 1);
                          console.log("Client successfully logged out");
                          worker.generateToken(token =>{
                            let obj=new Object();
                            obj.name=req.body.name;
                            obj.idc=queryResult2[0].idc;
                            obj.token=token;
                            tokenArray.push(obj);
        
                            database.writeLoginAttempt(queryResult1[0].id, 1);
                            console.log("Client successfully logged in");
                            return res.status(200).send(JSON.stringify({name:obj.name, token:obj.token}));
                          });
                        }
                        else
                        {
                          console.log("CHECK IF USER LOGGED");
                          return res.status(401).send();
                        }
                      });
                    }
                    else
                    {
                      worker.generateToken(token =>{
                        let obj=new Object();
                        obj.name=req.body.name;
                        obj.idc=queryResult2[0].idc;
                        obj.token=token;
                        tokenArray.push(obj);
    
                        database.writeLoginAttempt(queryResult1[0].id, 1);
                        console.log("Client successfully logged in");
                        return res.status(200).send(JSON.stringify({name:obj.name, token:obj.token}));
                      });
                    }
                  });
                }
              });
            });
          }
          else
          {
            console.log("BAD LOGIN");
            database.writeLoginAttempt(queryResult1[0].id, 0);
            return res.status(401).send();
          }
      });
      }
      else
      {
        console.log("NOT EXISTS IN DATABASE");
        return res.status(401).send();
      }
    });
  });

  app.post('/logout', (req, res) => {
    worker.verifyUserToken(req, tokenArray, verified =>{
      if(verified > -1)
      {
        tokenArray.splice(verified, 1);
        console.log("Client successfully logged out");
        return res.status(200).send();
      }
      else
      {
        return res.status(401).send();
      }
    });
  });

  app.post('/userinfo', (req, res) => {
    worker.verifyUserToken(req, tokenArray, verified =>{
      console.log(verified);
      if(verified > -1)
      {
        database.getClientInfo(tokenArray[verified].idc, queryResult =>{
          if(queryResult != "Error" && queryResult.length != 0 && queryResult != undefined && queryResult != null)
          {
            queryResult = JSON.parse(queryResult);
            let obj = new Object();
            obj.ID = queryResult[0].id;
            obj.FirstName = queryResult[0].fname;
            obj.LastName = queryResult[0].lname;
            obj.Mail = queryResult[0].email;

            console.log("User info sent");
            return res.status(200).send(JSON.stringify(obj));
          }
          else
          {
            return res.status(401).send();
          }
        });
      }
      else
      {
        return res.status(401).send();
      }
    });
  });

  app.post('/accounts', (req, res) => {
    worker.verifyUserToken(req, tokenArray, verified =>{
      if(verified > -1)
      {
        database.getAccounts(req.body.id, queryResult =>{
          if(queryResult != "Error")
          {
            console.log("Accounts sent");
            return res.status(200).send(queryResult);
          }
          else
          {
            return res.status(401).send();
          }
        });
      }
      else
      {
        return res.status(401).send();
      }
    });
  });

  app.post('/accinfo', (req, res) => {
    worker.verifyUserToken(req, tokenArray, verified =>{
      if(verified > -1)
      {
        database.getAccountInfo(req.body.accNum, queryResult =>{
          if(queryResult != "Error")
          {
            console.log("Account info sent");
            return res.status(200).send(queryResult);
          }
          else
          {
            return res.status(401).send();
          }
        });
      }
      else
      {
        return res.status(401).send();
      }
    });
  });

  app.post('/transhistory', (req, res) => {
    worker.verifyUserToken(req, tokenArray, verified =>{
      if(verified > -1)
      {
        database.getTransHistory(req.body.idAcc, queryResult =>{
          if(queryResult != "Error")
          {
            console.log("Transaction history sent");
            return res.status(200).send(queryResult);
          }
          else
          {
            return res.status(401).send();
          }
        });
      }
      else
      {
        return res.status(401).send();
      }
    });
  });

  app.post('/cards', (req, res) => {
    worker.verifyUserToken(req, tokenArray, verified =>{
      if(verified > -1)
      {
        database.getCards(req.body.idAcc, queryResult =>{
          if(queryResult != "Error")
          {
            console.log("Cards sent");
            return res.status(200).send(queryResult);
          }
          else
          {
            return res.status(401).send();
          }
        });
      }
      else
      {
        return res.status(401).send();
      }
    });
  });

  app.post('/cardinfo', (req, res) => {
    worker.verifyUserToken(req, tokenArray, verified =>{
      if(verified > -1)
      {
        database.getCardInfo(req.body.idCard, queryResult =>{
          if(queryResult != "Error")
          {
            console.log("Card info sent");
            return res.status(200).send(queryResult);
          }
          else
          {
            return res.status(401).send();
          }
        });
      }
      else
      {
        return res.status(401).send();
      }
    });
  });

  app.post('/cardtrans', (req, res) => {
    worker.verifyUserToken(req, tokenArray, verified =>{
      if(verified > -1)
      {
        database.getCardTrans(req.body.idCard, queryResult =>{
          if(queryResult != "Error")
          {
            console.log("Card info sent");
            return res.status(200).send(queryResult);
          }
          else
          {
            return res.status(401).send();
          }
        });
      }
      else
      {
        return res.status(401).send();
      }
    });
  });

  app.post('/changepassword', (req, res) => {
    worker.verifyUserToken(req, tokenArray, verified =>{
      if(verified > -1)
      {
        database.getLoginData(req.body.name, req.body.oldPassword, queryResult2 =>{
          queryResult2 = JSON.parse(queryResult2);
          if(queryResult2.length != 0 && queryResult2 != undefined && queryResult2 != null && queryResult2 != "Error")
          {
            database.changePassword(req.body.id, req.body.newPassword, success =>{
              console.log(success);
              if(success)
              {
                worker.generateToken(tokenn =>{
                  tokenArray[verified].token = tokenn;
                  console.log("Password changed");
                  return res.status(200).send(JSON.stringify({token:tokenn}));
                });
              }
              else
              {
                return res.status(401).send();
              }
            });
          }
          else
          {
            return res.status(401).send();
          }
        });
      }
      else
      {
        return res.status(401).send();
      }
    });
});

app.post('/payment', (req, res) => {
  worker.verifyUserToken(req, tokenArray, verified =>{
    console.log(req.body.token);
    if(verified > -1)
    {
      database.payment(req.body.senderAcc, req.body.recieverAcc, req.body.amount, success =>{
        if(success)
        {
          database.getAccountInfo(req.body.senderAcc, result =>{
            if(result != "Error")
            {
              return res.status(200).send(result);
            }
            else
            {
              console.log("GET ACCOUNT INFO APPJS");
              return res.status(401).send();
            }
          });
        }
        else
        {
          console.log("USER NOT SUCCESS IN PAYMENT");
          return res.status(401).send();
        }
      });
    }
    else
    {
      console.log("USER NOT VERIFIED");
      return res.status(401).send();
    }
  });
});

app.listen(3125, () =>
  console.log('Server started on port 3125'),
);