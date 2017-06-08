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
        
        <link href="css/newsTiles.css" rel="stylesheet">
        <link href="css/profile.css" rel="stylesheet">
        
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/modernizr.js" type="text/javascript"></script>
        
        <a href="WEB-INF/jspf/articleControls.jspf"></a>
        
        <script type="text/javascript">
            $(document).ready( function(){
                $(".navButton").removeClass("active");
                $("#profileButton").addClass("active");
            });
            
            function getBlocked(username){
                $('#blockTable').empty();
                $.ajax({
                    type: "POST",
                    url: "fetch_blocked",
                    data:{
                        username: username
                    }
                }).done(function(data){
                    $('#blockTable').html(data);
                });
            }
            
            function changeCategory(element){
               var thisId = element.id;
                switch(thisId){
                    case "newsButton":
                            append = "./#1";
                            break;
                        case "businessButton":
                            append = "./#2";
                            break;
                        case "scienceButton":
                            append = "./#3";
                            break;
                        case "sportButton":
                            append = "./#4";
                            break;
                        case "gamingButton":
                            append = "./#5";
                            break;
                        case "musicButton":
                            append = "./#6";
                            break;
                        case "entertainButton":
                            append = "./#7";
                            break;
                        case "readingListButton":
                            append = "./#8";
                            break;
                        case "allNewsButton":
                            append = "./#";
                            break;
                    }
               location.href = append;
           }
        </script>
    </head>
    
    <%@include file="WEB-INF/jspf/header.jspf" %>
    <%@include file="WEB-INF/jspf/sideBar.jspf" %>
        
    <body>
        <div class="container">
            <div id="blockTable">
                
            </div>
        </div>        
    </body>
    <footer>
        <script>
            window.onload = function(){ <%
                if(lg == null){
                    response.sendRedirect("login");
                }
                else{ %>
                    getBlocked("<%=lg.getUsername()%>"); <%
                } %>
            };
            
            function block(feed_id, count){
                element = $('#block-glyphicon' + count); <%
                if(lg != null){ %>
                    $.ajax({
                        type: "POST",
                        url: "block",
                        data:{
                            feedId: feed_id
                        }
                    }).done(function(){
                        element.toggleClass('blockActive');
                    }); <%
                }
                else{ %>
                    $('#loginModal').modal('show'); <%
                } %>
            }
        </script>
    </footer>
</html>
