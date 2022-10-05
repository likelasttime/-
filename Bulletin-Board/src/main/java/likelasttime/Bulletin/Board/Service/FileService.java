package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.FileRepository;
import likelasttime.Bulletin.Board.domain.posts.FileDto;
import likelasttime.Bulletin.Board.domain.posts.UploadFile;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class FileService {
    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    public String fileSave(String rootLocation, MultipartFile file) throws IOException {
        File uploadDir = new File(rootLocation);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // saveFileName 생성
        UUID uuid = UUID.randomUUID();
        String saveFileName = uuid.toString() + file.getOriginalFilename();
        File saveFile = new File(rootLocation, saveFileName);
        FileCopyUtils.copy(file.getBytes(), saveFile);

        return saveFileName;
    }

    public UploadFile store(MultipartFile file) throws Exception {
        try {
            if(file.isEmpty()) {
                throw new Exception("Failed to store empty file " + file.getOriginalFilename());
            }
            Path rootLocation= Paths.get("C:/image/");
            String saveFileName = fileSave(rootLocation.toString(), file);
            FileDto fileDto = FileDto.builder()
                    .originFileName(file.getOriginalFilename())
                    .uuidFileName(saveFileName)
                    .contentType(file.getContentType())
                    .fileSize(file.getResource().contentLength())
                    .registerDate(LocalDateTime.now())
                    .filePath(rootLocation.toString().replace(File.separatorChar, '/') +'/' + saveFileName)
                    .build();
            UploadFile saveFile=modelMapper.map(fileDto, UploadFile.class);
            fileRepository.save(saveFile);
            return saveFile;

        } catch(IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    public UploadFile load(Long fileId) {
        return fileRepository.findById(fileId).get();
    }

}
