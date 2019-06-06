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