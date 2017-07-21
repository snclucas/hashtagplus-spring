var stompClient = null;



function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);

        console.log('Connected: ' + frame);
        sendName();
        stompClient.subscribe('/topic/greetings', function (greeting) {
            console.log(greeting.body);
            constructMessageList(JSON.parse(greeting.body));
        });
        stompClient.subscribe('/topic/greetings2', function (greeting) {
            console.log(greeting.body);
        });
    });

}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message.title + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
    $( "#tester" ).click(function() { askHashtags(); });
});


$( document ).ready(function() {
    connect();
});

function constructMessageList(messages) {
    var contentLength = $('ul#message-list > li').length;
    //$('ul#message-list').empty();
    messages.forEach(function(message){
        var content = makeMessage(message.title, message.text);
        $('ul#message-list').append(content);
    });

}


function makeMessage(title, text) {

 var html = ' \
   <li th:each="message : ${messages}"> \
      <time datetime="2014-07-20"> \
      <span class="day">4</span> \
      <span class="month">Jul</span> \
       <span class="year">2014</span> \
        <span class="time">ALL DAY</span> \
    </time> \
    <img alt="Independence Day" src="https://farm4.staticflickr.com/3100/2693171833_3545fb852c_q.jpg" /> \
        <div class="info"> \
        <h2 class="title">'+title+'</h2> \
        <p class="desc">'+text+'</p> \
        <ul> \
        <li style="width:33%;">1 <span class="glyphicon glyphicon-ok"></span></li> \
        <li style="width:33%;">103 <span class="fa fa-envelope"></span></li> \
        </ul> \
        </div> \
        <div class="social"> \
        <ul> \
        <li class="edit" style="width:33%;"><a href="#"><span class="glyphicon glyphicon-chevron-up"></span></a></li> \
        <li class="confirm" style="width:34%;"><a href="#"><span class="glyphicon glyphicon-chevron-down"></span></a></li> \
        <li class="delete" style="width:33%;"><a href="#"><span class="fa fa-trash-o"></span></a></li> \
        </ul> \
        </div> \
        </li> \
';
return html;

}

function askHashtags() {

    stompClient.send("/app/hashtags", {}, JSON.stringify({'name': $("#name").val()}));

}

