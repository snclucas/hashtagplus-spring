function populateHashtagList(listId, data) {
  var urlParams = new URLSearchParams(window.location.search);
  $.each(data, function (id, hashtag) {
    var urlParams2 = new URLSearchParams(urlParams.toString());
    var hashtagJSON = JSON.parse(hashtag.hashtag);
    var hashtagClass = "notice-info";
    var entries = urlParams.entries();
    var pair = null;
    var inUrlQuery = false;
    for (pair of entries) {
      if (pair[0] === 'hashtag' && pair[1] === hashtagJSON.text) {
        hashtagClass = "notice-danger";
        inUrlQuery = true;
        break;
      }
    }

    if(!inUrlQuery) {
      urlParams2.append('hashtag', hashtagJSON.text);
    } else {
      var orig = urlParams2.getAll('hashtag');
      var index = orig.indexOf(hashtagJSON.text);
      if (index > -1) {
        orig.splice(index, 1);
      }
      urlParams2.delete('hashtag');
      $.each(orig, function (id, entry) {
        urlParams2.append('hashtag', entry);
      })
    }
    var newUrl = window.location.protocol + "//" + window.location.host + window.location.pathname + '?' + urlParams2.toString();
    $(listId).append('<div class="notice ' + hashtagClass + '"><a class="hashtag-list" href="' + newUrl + '">#' + hashtagJSON.text + '(' + hashtag.count + ')</a></div>');
  })
  ;
}


$("body").on('click', '.hashtag', function () {
  console.log($(this).val());
});

$(function () {
  $.ajax({
    type: 'GET',
    url: '/api/hashtags/aggregate',
    dataType: "json",
    contentType: 'application/json',
    async: true,
    success: function (data) {
      populateHashtagList($("#hashtag-list"), data);
    }
  });
});