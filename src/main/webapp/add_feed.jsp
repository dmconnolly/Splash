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
        <link href="css/index.css" rel="stylesheet">
        
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/modernizr.js" type="text/javascript"></script>
        <script src="js/returnToTop.js" type="text/javascript"></script>
        
        
        <script type="text/javascript">
            $(document).ready( function(){
                $(".navButton").removeClass("active");
            });

            $(document).ready( function(){
               $("#addFeedButton").addClass("active");
            });
        </script>

    </head>
    
    <%@include file="WEB-INF/jspf/header.jspf" %>
    <%@include file="WEB-INF/jspf/sideBar.jspf" %>
    
    <body>
        <div class="container">
            <div class="row">
                <div class="col-sm-12">
                    <form method="POST" action="add_feed_">
                        <table>
                            <tbody>
                                <tr>
                                    <td>Feed Name</td>
                                    <td><input type="text" name="name" required="required"></td>
                                </tr>
                                <tr>
                                    <td>Category</td>
                                    <td><input type="text" name="category" required="required"></td>
                                </tr>
                                <tr>
                                    <td>URL</td>
                                    <td><input type="text" name="url" required="required"></td>
                                </tr>
                                <tr>
                                    <td><input type="submit" value="Add feed"></td>
                                    <td><!----></td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
