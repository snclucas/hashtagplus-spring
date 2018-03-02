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
        window.location.reload();
      }
    });
  })


});

function deleteMessage(id) {
  $.ajax({
    type: 'POST',
    url: '/api/messages/' + id + '/delete',
    contentType: 'application/json',
    dataType: "json",
    async: true,
    success: function (data) {
      window.location.reload();
    },
    error: function (error) {
      console.log(error);
    }
  });
}



function setButtonStrip() {
  var urlParams = new URLSearchParams(window.location.search);

  var orderValue = urlParams.get('order');

  if (orderValue === 'asc') {
    $('#btn_order_asc').addClass('active');
    $('#btn_order_desc').removeClass('active')
  }
  else {
    $('#btn_order_asc').removeClass('active');
    $('#btn_order_desc').addClass('active');
  }

  var sortValue = urlParams.get('sort');

  if (sortValue === 'score') {
    $('#btn_sort_by_score').addClass('active');
    $('#btn_sort_by_date').removeClass('active');
  }
  else {
    $('#btn_sort_by_score').removeClass('active');
    $('#btn_sort_by_date').addClass('active');
  }

  var privacy = urlParams.get('privacy');

  if (privacy === 'private') {
    $('#btn_privacy').addClass('active');
  }
  else {
    $('#btn_privacy').removeClass('active');
  }

  var tab = urlParams.get('tab');

  $('#list_tab').removeClass('active')
  $('#events_tab').removeClass('active')
  $('#media_tab').removeClass('active')

  $('#list_tab_content').hide();
  $('#events_tab_content').hide();
  $('#media_tab_content').hide();

  if (tab === 'media') {
    $('#media_tab').addClass('active');
    $('#media_tab_content').show();
  } else if (tab === 'events') {
    $('#events_tab').addClass('active')
    $('#events_tab_content').show();
  } else {
    $('#list_tab').addClass('active')
    $('#list_tab_content').show();
  }

}

function getPathFromUrl(url) {
  return url.split("?")[0];
}


function clearTopic() {
  var urlSearch = window.location.search;
  var path = window.location.pathname.split("/")[1];
  window.location.href = window.location.protocol + "//" + window.location.host + "/" + path + urlSearch;
}

function clearHashtags() {
  var urlParams = new URLSearchParams(window.location.search);
  urlParams.delete('hashtag');
  var newUrl = window.location.protocol + "//" + window.location.host + window.location.pathname + '?' + urlParams.toString();
  window.location.href = newUrl;
}


$(function () {
  setButtonStrip();

  $("form").on('submit', function (e) {
    e.preventDefault();
  });

  $("li[data-delete-message]").click(function (e) {
    e.preventDefault();
    var result = confirm("Want to delete?");
    if (result) {
      var id = $(this).data('delete-message');
      deleteMessage(id);
    }

  });


  $("#clear_topic").click(function () {
    clearTopic();
  });
  $("#clear_hashtags").click(function () {
    clearHashtags();
  });

  $("#btn_sort_by_score").click(function () {
    var urlParams = new URLSearchParams(window.location.search);
    urlParams.set('sort', 'score');
    window.location.search = urlParams;
  });

  $("#btn_privacy").click(function () {
    var urlParams = new URLSearchParams(window.location.search);

    if ($("#btn_privacy").hasClass("active")) {
      urlParams.set('privacy', 'public');
    } else {
      urlParams.set('privacy', 'private');
    }
    window.location.search = urlParams;
  });

  $("#btn_sort_by_date").click(function () {
    var urlParams = new URLSearchParams(window.location.search);
    urlParams.set('sort', 'date');
    window.location.search = urlParams;
  });

  $("#btn_order_asc").click(function () {
    var urlParams = new URLSearchParams(window.location.search);
    urlParams.set('order', 'asc');
    window.location.search = urlParams;
  });

  $("#btn_order_desc").click(function () {
    var urlParams = new URLSearchParams(window.location.search);
    urlParams.set('order', 'desc');
    window.location.search = urlParams;
  });

  $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    var target = $(e.target).attr("data-tab");// activated tab
    var urlParams = new URLSearchParams(window.location.search);
    urlParams.set('tab', target);
    var newUrl = window.location.protocol + "//" + window.location.host + window.location.pathname + '?' + urlParams.toString();
    history.replaceState({foo: "bar"}, "page 3", newUrl);


    $('#list_tab_content').hide();
    $('#events_tab_content').hide();
    $('#media_tab_content').hide();

    if (target === 'media') {
      $('#media_tab_content').show();
    } else if (target === 'events') {
      $('#events_tab_content').show();
    } else {
      $('#list_tab_content').show();
    }


    $('#message_text').on('keypress', function (e) {
      console.log($('#message_text').val())
    });

// With JQuery
    $("#ex13").slider({
      ticks: [0, 100, 200, 300, 400],
      ticks_labels: ['$0', '$100', '$200', '$300', '$400'],
      ticks_snap_bounds: 30
    });


  });

  $("#set_topic_modal_button").click(function () {
    var topic = $("#set_topic_modal_topictext").val();
    var urlSearch = window.location.search;
    var path = window.location.pathname.split("/")[1];
    window.location.href = window.location.protocol + "//" + window.location.host + "/" + path + "/" + topic + urlSearch;

    $('#modalwindow').modal('hide');
  });
});