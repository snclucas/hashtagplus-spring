$(function () {
    $.ajax({
        type: 'GET',
        url: '/api/hashtags',
        dataType: "json",
        contentType: 'application/json',
        async: true,
        success: function (data) {
            console.log(data);
        }
    });
});