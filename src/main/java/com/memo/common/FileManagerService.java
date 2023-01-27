package com.memo.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component	// bo, dao, contorller의 부모격 (일반적인 스프링 빈으로 성격이 딱히 없을때 사용)
public class FileManagerService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
	
	public void deleteFile(String imagePath) { // imagePath: /images/aaaa_16205468768/sun.png
		//         \images/   imagePath에 있는 겹치는 /images/ 구문 제거
		Path path = Paths.get(FILE_UPLOAD_PATH + imagePath.replace("/images/", "")); // path= 이미지
		if(Files.exists(path)) {
			// 이미지 삭제 (bo는 삭제되든 안 되든 상관없으므로, 현재 페이지에서 에러 수긍)
			try {
				Files.delete(path);
			} catch (IOException e) {
				logger.error("[이미지 삭제] 이미지 삭제 실패. imagePath:{}" + imagePath); // 실패했지만 디렉토리 삭제하기 위해 return 값 따로 안 넣음
			} 
			
			// 디렉토리(폴더) 삭제
			path = path.getParent(); // 현재 이미지에 있던 폴더로 올라감 (부모 폴더로 한단계 올라감)
			if(Files.exists(path)) {
				try {
					Files.delete(path);
				} catch (IOException e) {
					logger.error("[이미지 삭제] 디렉토리 삭제 실패. imagePath:{}" + imagePath);
				}
			}
			
		}
		
		
		
	}
	
}
