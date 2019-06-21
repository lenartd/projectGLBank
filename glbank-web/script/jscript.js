function login()
{
    let uname = $("#email").val();
    let pass = $("#password").val();

    $.ajax({
        url: 'http://localhost:3125/login',
        dataType: 'json',
        type: 'post',
        headers:{"Content-Type":"application/json"},
        data: JSON.stringify({name:uname, password:pass}),
        success: function( data, textStatus, jQxhr ){
            sessionStorage.setItem("name", data.name);
            sessionStorage.setItem("token", data.token);
            console.log(data.token);
            window.location.replace("home.html");
        },
        error: function( jqXhr, textStatus, errorThrown ){
            console.log( errorThrown );
        }
    });
}

function toPayment()
{
    window.location.replace("payment.html");
}

function logout()
{
    let uname = sessionStorage.getItem("name"); 
    let utoken = sessionStorage.getItem("token"); 
    
    $.ajax({
        url: 'http://localhost:3125/logout',
        dataType: 'json',
        type: 'post',
        headers:{"Content-Type":"application/json"},
        data: JSON.stringify({name:uname, token:utoken}),
        success: function( data, textStatus, jQxhr ){
            sessionStorage.clear();
            window.location.replace("index.html");
        },
        error: function( jqXhr, textStatus, errorThrown ){
            console.log( errorThrown );
        }
    });
}

function isLogged()
{
    if(sessionStorage.getItem("name") == undefined || sessionStorage.getItem("token") == undefined)
    {
        window.location.replace("index.html");
    }
    else
    {
        getUserInfo();
        console.log(sessionStorage.getItem("name"));
        console.log(sessionStorage.getItem("userInfo"));
        let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
        document.getElementById("uname").innerHTML += " " + userInfo.FirstName + " " + userInfo.LastName;
        getAccounts(res =>{
            for(let i = 0; i < res.length; i++)
            {
                $("#accs").append($("<option>").attr({'value': res[i].AccNum, 'id': res[i].AccNum}).text(res[i].AccNum));
            }
            getAccInfo2(document.getElementById("accs").value, res=>{
                document.getElementById("userbal").innerHTML += res[0].amount + ' €';
                getTransHistory(res[0].id);
            });
        });
    }
}

function accOnLoad()
{


    if(sessionStorage.getItem("name") == undefined || sessionStorage.getItem("token") == undefined)
    {
        window.location.replace("index.html");
    }
    else
    {
        getAccounts(res =>{
            let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
            let accs = JSON.parse(sessionStorage.getItem("accnums"));
            console.log(accs.length);   
            console.log(accs);
            for(let i = 0; i<accs.length; i++)
            {
                $('#mainDiv').append("<div  style='margin-top:15px; height:150px;' id='accDiv"+ i +"' class='innerDiv'></div>");
    
                $('#accDiv' + i).append("<p class='userFont'>User Name: " + userInfo.FirstName +" "+ userInfo.LastName +"</p>");
    
                getAccInfo2(accs[i].AccNum, result =>{
                    $('#accDiv' + i).append("<p class='userFont'>Account Number: " + accs[i].AccNum +"</p>");
    
                    $('#accDiv' + i).append("<p class='userFont'>Current Balance: " + result[0].amount + " €</p>");

                    /*
                    getCards(accs[i].AccNum, res =>{
                        console.log(res);
                        $('#accDiv' + i).append("<p class='userFont'>Card Active: " + result[0].active + "</p>");
                        $('#accDiv' + i).append("<p class='userFont'>Expiration: " + result[0].expireM +"/" + result[0].expireY + "</p>");
                        $('#accDiv' + i).append("<button style='position: absolute; bottom: 0; right: 0;' type='button'>Payment</button>");
                    });
                    */
                    $('#accDiv' + i).append("<button style='position: absolute; bottom: 0; right: 0;' type='button' onclick='toPayment()'>Payment</button>");
                });
            }
        });
       
    }
}

function accInfoOnLoad()
{
    if(sessionStorage.getItem("name") == undefined || sessionStorage.getItem("token") == undefined)
    {
        window.location.replace("index.html");
    }
    else
    {
        let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
        document.getElementById("fname").innerHTML += userInfo.FirstName;
        document.getElementById("lname").innerHTML += userInfo.LastName;
        document.getElementById("mail").innerHTML += userInfo.Mail;
    }
}

function getAccounts(res)
{
    let uname = sessionStorage.getItem("name"); 
    let utoken = sessionStorage.getItem("token");
    let uid = JSON.parse(sessionStorage.getItem("userInfo")).ID;

    $.ajax({
        url: 'http://localhost:3125/accounts',
        dataType: 'json',
        type: 'post',
        headers:{"Content-Type":"application/json"},
        data: JSON.stringify({name:uname, id:uid, token:utoken}),
        success: function( result, textStatus, jQxhr ){
            console.log(result);
            sessionStorage.setItem("accnums", JSON.stringify(result));
            res(result);
        },
        error: function( jqXhr, textStatus, errorThrown ){
            console.log( errorThrown );
        }
    });
}

function changeAccInfos()
{
    getAccInfo2(document.getElementById("accs").value, res =>{
        //console.log(res);
        document.getElementById("userbal").innerHTML = "Current Balance: " + res[0].amount + " €";
        getTransHistory(res[0].id);
    });
}

function changeAccInfos2()
{
    getAccInfo2(document.getElementById("accs2").value, res =>{
        //console.log(res);
        document.getElementById("cbalancePaym").innerHTML = "Current Balance: " + res[0].amount + " €";
    });
}


function getAccInfo2(acc, res)
{
    let uname = sessionStorage.getItem("name"); 
    let utoken = sessionStorage.getItem("token");
    let accnum = acc;

    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:3125/accInfo",
        data: JSON.stringify({name:uname, accNum:accnum, token:utoken}),
        success: (result) => {
            result = JSON.parse(result);
            res(result);
        },
        error: (xhr) => { 
            console.log(xhr.status);
        }	
    });
}

function getTransHistory(idacc)
{
    let aid = idacc;
    let uname = sessionStorage.getItem("name");
    let utoken = sessionStorage.getItem("token");

    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: "http://localhost:3125/transHistory",
        data: JSON.stringify({idAcc: aid, name: uname, token: utoken}),
        success: (result) => {  
            fillTransHistory(result);
        },
        error: (xhr) => { 
            console.log(xhr.status);
        }	
    });
}

function fillTransHistory(result)
{
    console.log("TRANSACTION " + result);
    result = JSON.parse(result);
    $('#transactionDiv').empty();
    let index = 1;
    for(let i = 0; i<result.length; i++)
    {
        $('#transactionDiv').append("<div class='transaction' id='transaction"+ i +"' class='transaction'></div>");
        $('#transaction' + i).append("<p style='margin-left: 30px;'>" + index +", Reciever account: " + result[i].RecAccount +"   Amount: "+ result[i].TransAmount + ' €'+"</p>");
        index++;
    }
    console.log(result);
}

function getCards(accid, res)
{
    let uname = sessionStorage.getItem("name"); 
    let utoken = sessionStorage.getItem("token");

    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: 'http://localhost:3125/cards',
        data: JSON.stringify({name:uname, token:utoken, idAcc:accid}),
        success: (result) => {
           res(JSON.parse(result));
        },
        error: (xhr) => { 
            console.log(xhr.status);
        }	
    });
}

function getCardInfo(cardid, res)
{
    let uname = sessionStorage.getItem("name"); 
    let utoken = sessionStorage.getItem("token");
    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: 'http://localhost:3125/cards',
        data: JSON.stringify({name:uname, token:utoken, idCard:cardid}),
        success: (result) => {
           res(JSON.parse(result));
        },
        error: (xhr) => { 
            console.log(xhr.status);
        }	
    });
}

function getUserInfo()
{
    let uname = sessionStorage.getItem("name"); 
    let utoken = sessionStorage.getItem("token");

    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: 'http://localhost:3125/userinfo',
        data: JSON.stringify({name:uname, token:utoken}),
        success: (result) => {
            sessionStorage.setItem("userInfo", result);
        },
        error: (xhr) => { 
            console.log(xhr.status);
        }	
    });
}

function changePassword(opass, npass)
{
    let uname = sessionStorage.getItem("name"); 
    let utoken = sessionStorage.getItem("token");
    let uid = JSON.parse(sessionStorage.getItem("userInfo")).ID;

    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: 'http://localhost:3125/changepassword',
        data: JSON.stringify({name:uname, token:utoken, oldPassword:opass, newPassword:npass, id:uid}),
        success: (result) => {
            result = JSON.parse(result);
            sessionStorage.setItem("token", result.token);
            document.getElementById("pwchangeDiv").style.display = "none";
            document.getElementById("cpw").value = " ";
            document.getElementById("npw").value = " ";
            alert("Password changed");
        },
        error: (xhr) => { 
            console.log(xhr.status);
        }	
    });
}

function changeUserPassword()
{
    let oldPassword = document.getElementById("cpw").value;
    let newPassword = document.getElementById("npw").value;


    console.log(oldPassword);

    if(oldPassword != undefined && oldPassword != " " && oldPassword != null && newPassword != undefined && newPassword != " " && newPassword)
    {
        changePassword(oldPassword, newPassword);
    }
    else
    {
        alert("Password not changed");
    }
}

function showpwchange()
{
    document.getElementById("pwchangeDiv").style.display = "block";
}

function paymentOnLoad()
{
    if(sessionStorage.getItem("name") == undefined || sessionStorage.getItem("token") == undefined)
    {
        window.location.replace("index.html");
    }
    else
    {
        getUserInfo();

        let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
        document.getElementById("uname2").innerHTML += " " + userInfo.FirstName + " " + userInfo.LastName;
        getAccounts(res =>{
            for(let i = 0; i < res.length; i++)
            {
                $("#accs2").append($("<option>").attr({'value': res[i].AccNum, 'id': res[i].AccNum}).text(res[i].AccNum));
            }
            getAccInfo2(document.getElementById("accs2").value, res=>{
                document.getElementById("cbalancePaym").innerHTML += res[0].amount + ' €';
            });
        });
    }
}

function payIt()
{
    let recieveracc = document.getElementById("recieverAcc").innerHTML;
    let uamount = document.getElementById("amountPay").value;

    console.log(uamount);

    if(recieveracc != null && recieveracc != undefined && uamount > 0)
    {
        sendPayment(uamount, resp =>{
            if(resp != undefined)
            {
                document.getElementById("cbalancePaym").innerHTML = "Current Balance: " + resp[0].amount + " €";
                alert("Payment was successfull");
            }
            else
            {
                alert("Error in payment1");
            }
        });
    }
    else
    {
        alert("Error in payment2");
    }
    
}

function sendPayment(amontt, res)
{
    let uname = sessionStorage.getItem("name"); 
    let utoken = sessionStorage.getItem("token");
    let recieveracc = document.getElementById("recieverAcc").value;
    let senderacc = document.getElementById("accs2").value;

    let uamount = amontt;

    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url: 'http://localhost:3125/payment',
        data: JSON.stringify({name: uname, recieverAcc: recieveracc, senderAcc: senderacc, amount: uamount, token: utoken}),
        success: (result) => {
            result = JSON.parse(result);
            console.log(result);
            res(result);
        },
        error: (xhr) => { 
            console.log(xhr.status);
        }	
    });
}