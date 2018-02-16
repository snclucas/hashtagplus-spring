$(function () {

  $("#add_message_form").submit(function (e) {
    e.preventDefault();
    $.ajax({
      type: 'POST',
      url: '/api/messages/add',
      dataType: "json",
      //contentType: 'application/json',
      data: $("#add_message_form").serialize(),
      async: true,
      success: function (data) {
        console.log("dddd");
        window.location.reload();
      }
    });
  })
});

function getPathFromUrl(url) {
    return url.split("?")[0];
}


function clearTopic() {
    var urlSearch = window.location.search;
    var path = window.location.pathname.split("/")[1];
    window.location.href = window.location.protocol + "//" + window.location.host + "/" + path + urlSearch;
}

function clearHashtags() {
    var url = getPathFromUrl(window.location.href);
    window.location.href = url;
}


$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#clear_topic" ).click(function() { clearTopic(); });
    $( "#clear_hashtags" ).click(function() { clearHashtags(); });
});