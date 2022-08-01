package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.FileRepository;
import likelasttime.Bulletin.Board.domain.posts.File;
import likelasttime.Bulletin.Board.domain.posts.FileDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    public Long saveFile(FileDto fileDto){
        return fileRepository.save(modelMapper.map(fileDto, File.class)).getId();
    }

    public FileDto getFile(Long id){
        File file=fileRepository.findById(id).get();
        FileDto fileDto=modelMapper.map(file, FileDto.class);
        return fileDto;
    }

}
