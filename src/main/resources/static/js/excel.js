function checkFileSize() {
  var input = document.getElementById('fileInput');
  var fileSize = input.files[0].size; // 파일 크기 (바이트 단위)
  var maxSize = 10 * 1024 * 1024; // 최대 허용 파일 크기 (10MB)

  if (fileSize > maxSize) {
    // 파일 크기 초과 시 에러 메시지 표시
    document.getElementById(
        'fileSizeError').innerText = '파일 크기는 10MB 이하로 업로드 해주세요';
    return false; // 폼 제출 취소
  } else {
    // 파일 크기가 허용 범위일 때 에러 메시지 초기화
    document.getElementById('fileSizeError').innerText = '';
  }

  return true; // 폼 제출 허용
}