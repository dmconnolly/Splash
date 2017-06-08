<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        
        <title>Splash</title>
        
        <link rel="shortcut icon" href="favicon.ico">
        
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/glyphicons.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/fonts.css" rel="stylesheet">
        <link href="css/main.css" rel="stylesheet">
        <link href="css/forms.css" rel="stylesheet"/>
        
        <script src="js/jquery.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/modernizr.js"></script>
        <script src="js/returnToTop.js"></script>
        <script src="js/formValidate.js"></script>
        <script src="js/additional-methods.js"></script>
        <script src="js/jquery.validate.js"></script>
        
        <script type="text/javascript">
            $(document).ready( function(){
                $(".navButton").removeClass("active");
            });

            $(document).ready( function(){
               $("#loginButton").addClass("active");
            });
        </script>
    </head>
    
    <%@include file="WEB-INF/jspf/header.jspf" %>
    
    <body>
        <div class="container" id="formDiv">
            <form method="POST" class="inputForm" id="loginForm" action="login_">
                <fieldset class="form-group">
                    <label for="username">Username</label>
                    <div class="tick tickRight">
                        <a class="glyphicons glyphicons-ok green hidden"></a>
                        <a class="glyphicons glyphicons-remove red hidden"></a>
                        <input type="text" class="form-control formInput" name="username" placeholder="Username" required>
                        <label for="username" class="error">${registered}</label>
                    </div>
                </fieldset>
                <fieldset class="form-group">
                    <label for="password">Password</label>
                    <div class="tick tickRight">
                        <a class="glyphicons glyphicons-ok green hidden"></a>
                        <a class="glyphicons glyphicons-remove red hidden"></a>
                        <input type="password" class="form-control formInput" name="password" placeholder="Password" required>
                    </div>
                </fieldset>
                <input class="btn btn-primary formSubmit" type="submit" value="Submit">
            </form>
        </div>
    </body>
</html>
