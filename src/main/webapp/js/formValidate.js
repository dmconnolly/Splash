$(document).ready(function(){
    $('#registerForm').validate({
        rules:{
            username:{
                minlength: 3,
                maxlength: 10,
                required: true
            },
            password:{
                minlength: 6,
                maxlength: 25,
                required: true
            },
            name:{
                maxlength: 25,
                required: true
            }
        },
        highlight: function(element){
            $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
            $(element).closest('#tick').addClass('hidden');
        },
        success: function(element){
            $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
            $(element).closest('#tick').removeClass('hidden');
        }
    });
    
    $('#loginForm').validate({
        rules:{
            username:{
                required: true
            },
            password:{
                required: true,
            }
        },
        highlight: function(element){
            $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
            $(element).next('.glyphicons glyphicons-ok green').addClass('hidden');
            $(element).next('.glyphicons glyphicons-remove red').removeClass('hidden');
        },
        success: function(element){
            $(element).closest('.form-group').removeClass('has-error').addClass('has-success');
            $(element).next('.glyphicons glyphicons-ok green').removeClass('hidden');
            $(element).next('.glyphicons glyphicons-remove red').addClass('hidden');
            $(element).next('.error').remove();
        }
    });
});
