package com.kh.toy.common.file;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import com.kh.toy.common.code.Config;
import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.exception.HandleableException;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;

public class FileUtil {

	private final int MAX_SIZE = 1920 * 1080 * 10;

	@SuppressWarnings({ "rawtypes" })
	public MultiPartParams fileUpload(HttpServletRequest request) {

		List<FileDTO> fileDtoList = new ArrayList<FileDTO>();
		Map<String, List> paramMap = new HashMap<String, List>();

		// FileDTO, MultipartRequest를 담고있는 Map을 반환.

		try {

			// 3. 경로와 파일 이름 합쳐 파일을 저장, MultipartRequest 생성

			MultipartParser parser = new MultipartParser(request, MAX_SIZE);
			parser.setEncoding("UTF-8");
			Part part = null;

			while ((part = parser.readNextPart()) != null) {

				if (part.isFile()) {
					
					// MuiltpartParser는 forM태그에 input[type=file]이 존재만 하면, 사용자가 파일을 첨부하지 않더라도
					// 빈 FilePart 객체를 생성해서 MuiltpartParser 객체에 넘겨준다.
 					// 사용자가 파일을 첨부하지 않은 경우에는 getFileName 메서드 결과가 Null임.
					FilePart filePart = (FilePart) part;
					
					if (filePart.getFileName() != null) {
						FileDTO fileDTO = createFileDTO(filePart);
						uploadFile(filePart, fileDTO);
						fileDtoList.add(fileDTO);
					}
				} else {
					ParamPart paramPart = (ParamPart) part;
					setParameterMap(paramPart, paramMap);
					
				}

			}

			// 5. 3에서 생성한 MultipartRequest와 List<FileDTO>를 반환.
			paramMap.put("files", fileDtoList);

		} catch (IOException e) {
			throw new HandleableException(ErrorCode.FAILED_FILE_UPLOAD);
		}
		return new MultiPartParams(paramMap);

	}

	private String createSubPath() {

		// 2. 파일 업로드 날자 기준으로 저장될 파일 경로 생성(날자)
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DAY_OF_MONTH);

		String subPath = year + "\\" + month + "\\" + date + "\\";

		return subPath;
	}

	private String createUploadPath(String subPath) {

		// 2. 파일 업로드 날자 기준으로 저장될 파일 경로 생성
		String uploadPath = Config.UPLOAD_PATH.DESC + subPath;

		File dir = new File(uploadPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		return uploadPath;
	}

	private FileDTO createFileDTO(FilePart filePart) {

		String renameFileName = UUID.randomUUID().toString(); // 1. 서버에 저장될 유니크한 파일이름 생성
		String originFileName = filePart.getFileName(); // 4. File_INFO 테이블에 저장할 FileDTO 생성
		String savePath = createSubPath();

		FileDTO fileDTO = new FileDTO();
		fileDTO.setOriginFileName(originFileName);
		fileDTO.setRenameFileName(renameFileName);
		fileDTO.setSavePath(savePath);

		return fileDTO;

	}
	
	private void uploadFile(FilePart filePart, FileDTO fileDTO) throws IOException {
		File file = new File(createUploadPath(fileDTO.getSavePath()) + fileDTO.getRenameFileName());
		filePart.writeTo(file);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setParameterMap(ParamPart paramPart, Map<String, List> paramMap) throws UnsupportedEncodingException {
		
		// 파라미터명으로 기존에 저장된 데이터가 없다.
		// 새로운 List를 생성해서 파라미터 value를 저장
		if (!paramMap.containsKey(paramPart.getName())) {
			List<String> params = new ArrayList<String>();
			params.add(paramPart.getStringValue());
			paramMap.put(paramPart.getName(), params);
		} else {
			paramMap.get(paramPart.getName()).add(paramPart.getStringValue());

		}
	}

}
