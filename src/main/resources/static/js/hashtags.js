function populateHashtagList(listId, data) {
  var items = [];
  $.each(data, function (id, hashtag) {
      var url = new URL(window.location.href);


    var hashtagJSON = JSON.parse(hashtag.hashtag);
      url.searchParams.append('hashtags', hashtagJSON.text);

      console.log(url.searchParams.get("hashtags"));

      $(listId).append('<div class="update-nag">' +
          '<div class="update-split"><i class="glyphicon glyphicon-refresh"></i></div>' +
          '<div class="update-text"><a href="'+url.toString()+'"><strong>'+ hashtagJSON.text + '('+hashtag.count+')</strong></a></div>' +
          '</div>');
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