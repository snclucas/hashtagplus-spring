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

}

$(function () {
  setButtonStrip();

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
});