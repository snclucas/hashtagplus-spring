function populateHashtagList(listId, data) {
  var items = [];
  $.each(data, function (id, hashtag) {
    var hashtagJSON = JSON.parse(hashtag.hashtag);
    $(listId).append('<div class="notice notice-danger"><strong>'+ hashtagJSON.text +'</strong> ('+hashtag.count+')</div>');
  });
}


$(function () {
  $.ajax({
    type: 'GET',
    url: '/api/hashtags/aggregate',
    dataType: "json",
    contentType: 'application/json',
    async: true,
    success: function (data) {
      console.log(data);
      populateHashtagList($("#hashtag-list"), data);
    }
  });
});