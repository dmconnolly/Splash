<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Splash</title>
        
        <link rel="icon" href="images/favicon.ico" />

        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/glyphicons.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
        <link href="css/fonts.css" rel="stylesheet">
        <link href="css/main.css" rel="stylesheet">
        <link href="css/index.css" rel="stylesheet">
        
        <script src="js/jquery.min.js" type="text/javascript"></script>
        <script src="js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/modernizr.js" type="text/javascript"></script>
        
        <script src="js/index.js" type="text/javascript"></script>
        <script src="js/returnToTop.js" type="text/javascript"></script>
        <script src="js/checkPreviousLocation.js" type="text/javascript"></script>
    </head>
    
    <%@include file="WEB-INF/jspf/header.jspf" %>
    <%@include file="WEB-INF/jspf/sideBar.jspf" %>
    <%@include file="WEB-INF/jspf/loginModal.jspf" %>
    
    <body>
        <div class="container" id="news-feed-container">
            <h2 id="categoryTitle"></h2>
            <div class="progress" id="loadingBar">
                <div class="progress-bar progress-bar-striped active" id="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100"></div>
            </div>
            <div id="news-feed">
                <!-- 
                This div is filled in by Javascript in index.js
                which makes an AJAX request to the articles servlet 
                -->
            </div>
        </div>
        <a class="glyphicons glyphicons-up-arrow returnTop" id="topArrow"></a>
    </body>
    <footer>
        <script type="text/javascript">
            function subscribe(feed_id, count){
                element = $('#subscribe-glyphicon' + count); <%
                if(lg != null){ %>
                    $.ajax({
                        type: "POST",
                        url: "subscribe",
                        data:{
                            feedId: feed_id
                        }
                    }).done(function(){
                        element.toggleClass('glyphicons-heart');
                        element.toggleClass('glyphicons-heart-empty');
                    }); <%
                }
                else{ %>
                    $('#loginModal').modal('show'); <%
                } %>
            }
            
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
            
            function readLater(article_id, count){
                element = $('#readLater-glyphicon' + count); <%
                if(lg != null){ %>
                    $.ajax({
                        type: "POST",
                        url: "readLater",
                        data:{
                            articleId: article_id
                        }
                    }).done(function(){
                        element.toggleClass('readLaterActive');
                    }); <%
                }
                else{ %>
                    $('#loginModal').modal('show'); <%
                } %>
            }

            window.onload = function(){ <%
                if(lg != null){ %>
                    //Check previous location Make sure to append # to href
                    checkPrev(); <%
                }else{ %>
                    setTitle("all");
                    get_articles("all"); <%
                } %>
            };
        </script>
    </footer>
</html>
