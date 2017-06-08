function checkPrev(){
    $(".navButton").removeClass("active");
    $(".sidebar-anchor").removeClass("active");
    if(window.location.href.indexOf("#1") > -1) {
        setTitle("news");
        get_articles("news");
        $("#newsButton").addClass("active");
    }
    else if(window.location.href.indexOf("#2") > -1) {
        setTitle("business and finance");
        get_articles("business and finance");
        $("#businessButton").addClass("active");
    }
    else if(window.location.href.indexOf("#3") > -1) {
        setTitle("science and technology");
        get_articles("science and technology");
        $("#scienceButton").addClass("active");
    }
    else if(window.location.href.indexOf("#4") > -1) {
        setTitle("sport");
        get_articles("sport");
        $("#sportButton").addClass("active");
    }
    else if(window.location.href.indexOf("#5") > -1) {
        setTitle("gaming");
        get_articles("gaming");
        $("#gamingButton").addClass("active");
    }
    else if(window.location.href.indexOf("#6") > -1) {
        setTitle("music");
        get_articles("music");
        $("#musicButton").addClass("active");
    }
    else if(window.location.href.indexOf("#7") > -1) {
        setTitle("entertainment and arts");
        get_articles("entertainment and arts");
        $("#entertainButton").addClass("active");
    }
    else if(window.location.href.indexOf("#8") > -1) {
        setTitle("readingList");
        get_articles("readingList");
        $("#readingListButton").addClass("active");
    }
    else if(window.location.href.indexOf("#") > -1) {
        setTitle("all");
        get_articles("all");
        $("#allNewsButton").addClass("active");
    }
    else{
        setTitle("userFeed");
        get_articles("userFeed");   
    }
}