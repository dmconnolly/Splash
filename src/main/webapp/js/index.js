function get_articles(category){
    showBar();
    $('#news-feed').empty();
    $.ajax({
        type: "POST",
        url: "articles",
        data:{
            category: category
        }
    }).done(function(data){
        hideBar();
        $('#news-feed').html(data.replace(/\uFFFD/g, ''));
    });
}

function hideBar(){
    $('#loadingBar').css("display", "none");
}

function showBar(){
    $('#loadingBar').css("display", "");
}

function toggle_expand(count){
    element = $('#toggle-expand-glyphicon' + count);
    element.toggleClass('glyphicons-chevron-down');
    element.toggleClass('glyphicons-chevron-up');
}

$(document).ready( function(){
    $(".navButton").removeClass("active");
    $(".sidebar-anchor").removeClass("active");
    $("#homeButton").addClass("active");
});

$(function(){
    $('[data-toggle="popover"]').popover();
});


function changeCategory(element){
    var thisId = element.id;
    $(".navButton").removeClass("active");
    $(".sidebar-anchor").removeClass("active");
    switch(thisId){
        case "newsButton":
            $("#newsButton").addClass("active");
            category = "news";
            break;
        case "businessButton":
            $("#businessButton").addClass("active");
            category = "business and finance";
            break;
        case "scienceButton":
            $("#scienceButton").addClass("active");
            category = "science and technology";
            break;
        case "sportButton":
            $("#sportButton").addClass("active");
            category = "sports";
            break;
        case "gamingButton":
            $("#gamingButton").addClass("active");
            category = "gaming";
            break;
        case "musicButton":
            $("#musicButton").addClass("active");
            category = "music";
            break;
        case "entertainButton":
            $("#entertainButton").addClass("active");
            category = "entertainment and arts";
            break;
        case "readingListButton":
            category = "readingList";
            $("#readingListButton").addClass("active");
            break;
        case "allNewsButton":
            category = "all";
            $("#allNewsButton").addClass("active");
            break;
    }
    $('#news-feed').empty();
    setTitle(category);
    get_articles(category);
}

function setTitle(title){
    switch(title){
        case "all":
            newTitle = "Latest Articles";
            break;
        case "news":
            newTitle = "Latest news";
            break;
        case "business and finance":
            newTitle = "Business and Finance";
            break;
        case "science and technology":
            newTitle = "Science and Technology";
            break;
        case "sports":
            newTitle = "Latest in Sport";
            break;
        case "gaming":
            newTitle = "Latest in Gaming";
            break;
        case "music":
            newTitle = "Latest in Music";
            break;
        case "entertainment and arts":
            newTitle = "Entertainment and Arts";
            break;
        case "userFeed":
            newTitle = "Subscribed Sources";
            break;
        case "readingList":
            newTitle = "Reading List";
            break;
    }
    $('#categoryTitle').empty();
    $('#categoryTitle').html(newTitle);
}