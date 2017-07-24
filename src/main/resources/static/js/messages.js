


$(function() {

    $("#add_message_form").submit(function(e) {
        e.preventDefault();
        $.ajax({
            type: 'POST',
            url: '/api/messages/add',
            dataType: "json",
            data: $("#add_message_form").serialize(),
            async: true,
            success: function(data) {
                window.location.reload();
            }
        });
    })
});