function populateHashtagList(data) {
  var urlParams = new URLSearchParams(window.location.search);
  $.each(data, function (id, hashtag) {
    var newUrlParams = new URLSearchParams(urlParams.toString());
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
      newUrlParams.append('hashtag', hashtagJSON.text);
    } else {
      var orig = newUrlParams.getAll('hashtag');
      var index = orig.indexOf(hashtagJSON.text);
      if (index > -1) {
        orig.splice(index, 1);
      }
      newUrlParams.delete('hashtag');
      $.each(orig, function (id, entry) {
        newUrlParams.append('hashtag', entry);
      })
    }

    var newUrl = window.location.protocol + "//" + window.location.host + window.location.pathname + '?' + newUrlParams.toString();
    var html = '<a class="hashtag-list" href="' + newUrl + '"><div class="notice ' + hashtagClass + '">#' + hashtagJSON.text + '(' + hashtag.count + ')</div></a>';

    if(inUrlQuery) {
      $("#hashtag_in_use-list").append(html);
    } else {
      $("#hashtag-list").append(html);
    }
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
      populateHashtagList(data);
    }
  });
});