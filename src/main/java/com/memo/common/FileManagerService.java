package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component	// bo, dao, contorller의 부모격 (일반적인 스프링 빈으로 성격이 딱히 없을때 사용)
public class FileManagerService {
	
	// 실제 이미지가 저장될 경로 (서버)
	public static final String FILE_UPLOAD_PATH = "C:\\eclipsehaeun\\6_spring_project\\memo\\workspace\\images/"; // 변경하지 못하도록 static final (상수, 상수는 보통 대문자)
	
	// input : MultipartFile, userLoginId 
	// output: image path(bo호출)
	public String saveFile(String userLoginId, MultipartFile file) {
		// 파일 디렉토리 예) aaaa_16205468768(userid_timestamp)/sun.png + 각 유저마다 폴더 생성 후 그 안에 img파일 넣기
		String directoryName = userLoginId + "_" + System.currentTimeMillis() + "/"; // aaaa_16205468768/
		String filePath = FILE_UPLOAD_PATH + directoryName; //C:\\eclipsehaeun\\6_spring_project\\memo\\workspace\\images/aaaa_16205468768/
		
		File directiory = new File(filePath); // 폴더 만들어낼 준비함.
		if(directiory.mkdir() == false) { // 폴더 만들어짐
			return null; // 폴더 만드는데 실패시 이미지패스 null
		}
		
		// 파일 업로드 : byte 단위로 업로드 된다.
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(filePath + file.getOriginalFilename()); // OriginalFilename은 사용자가 올린 파일명 / 한글이름은 안되므로, filePath + "fileName" 이런식으로 영어이름 붙여야한다.
			// 위에는 올릴거라는 명시, 아래는 진짜로 파일 올리는거
			Files.write(path, bytes); // 이런 path에 
			
		} catch (IOException e) {
			e.printStackTrace();
			return null; // 업로드 할 수 없던거니까
		}
		
		// 파일 업로드 성공했으면 이미지 url path를 리턴한다.
		// http://localhost/images/aaaa_16205468768/sun.png
		return "/images/" + directoryName + file.getOriginalFilename();
	}
	
	
	
}
