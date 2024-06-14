function sendVerificationEmail() {
  var email = document.getElementById("email").value;
  var xhr = new XMLHttpRequest();
  if (!email) {
    alert("세종대학교 이메일을 입력하세요.");
    return;
  }
  xhr.open("POST", "/user/send-verification-email", true);
  xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      if (xhr.responseText.includes("인증메일 발송에 성공했습니다.")) {
        document.getElementById(
            "verificationCodeSection1").style.visibility = "visible";
        document.getElementById(
            "verificationCodeSection2").style.visibility = "visible";
      }
      alert(xhr.responseText);
    }
  };
  var data = JSON.stringify({"email": email});
  xhr.send(data);
}

function verifyEmailCode() {
  var code = document.getElementById("verifyCode").value;
  var email = document.getElementById("email").value;
  if (!code) {
    alert("인증번호를 입력하세요.");
    return;
  }
  var xhr = new XMLHttpRequest();
  xhr.open("POST", "/user/verify-code", true);
  xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      if (xhr.responseText.includes("인증번호 확인에 성공했습니다.")) {
        document.getElementById(
            "verificationCodeSection1").getElementsByTagName(
            "input")[0].disabled = true;
        document.getElementById(
            "verificationCodeSection2").getElementsByTagName(
            "button")[0].disabled = true;
      }
      alert(xhr.responseText);
    }
  };
  var data = JSON.stringify({"email": email, "verifyCode": code});
  xhr.send(data);
}

function verifyStatus() {
  var email = document.getElementById("email").value;
  var xhr = new XMLHttpRequest();
  xhr.open("POST", "/user/verify-status", true);
  xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      if (xhr.responseText.includes("인증된 이메일입니다.")) {
        document.querySelector('form').submit();
      } else {
        alert(xhr.responseText);
      }
    }
  };
  var data = JSON.stringify({"email": email});
  xhr.send(data);
}

function clearCertification() {
  var email = document.getElementById("email").value;

  var xhr = new XMLHttpRequest();
  xhr.open("POST", "/user/certification/clear", true);
  xhr.setRequestHeader("Content-Type", "application/json");

  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4) {
      if (xhr.status === 200) {
        alert(xhr.responseText);
      } else {
        alert("초기화에 실패했습니다.");
      }
    }
  };

  var data = JSON.stringify({"email": email});
  xhr.send(data);
}
