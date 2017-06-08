jQuery(document).ready(function() {
    var offset = 100;
    var transition = 750;
    jQuery(window).scroll(function() {
        if (jQuery(this).scrollTop() > offset) {
            jQuery('.returnTop').fadeIn(transition);
        } else {
            jQuery('.returnTop').fadeOut(transition);
        }
    });
    jQuery('.returnTop').click(function(event) {
        event.preventDefault();
        jQuery('html, body').animate({scrollTop: 0}, transition);
        return false;
    })
});
