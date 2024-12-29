package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.exception.UserWithEmailNotFoundException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImageService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ImageService imageService;
    private final AdsMapper adsMapper;
    private final CommentMapper commentMapper;

        @Override
    public ResponseWrapperAds getAllAds() {
        List<Ads> adsList = adsRepository.findAll();
        List<AdsDto> adsDtoList = adsMapper.toDtos(adsList);
        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        responseWrapperAds.setCount(adsList.size());
        responseWrapperAds.setResults(adsDtoList);
        return responseWrapperAds;
    }


    @Override
    public ResponseWrapperAds getAdsMe(String email) {
        List<Ads> adsList = adsRepository.findByUser(userRepository.findByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email)));
        List<AdsDto> adsDtoList = adsMapper.toDtos(adsList);
        ResponseWrapperAds responseWrapperAds = new ResponseWrapperAds();
        responseWrapperAds.setResults(adsDtoList);
        responseWrapperAds.setCount(adsList.size());
        return responseWrapperAds;
    }


    @Override
    public AdsDto addAd(CreateAds createAds, String email, MultipartFile image) {
        Ads ads = adsMapper.toAdsFromCreateAds(createAds);
        ads.setUser(userRepository.findByEmail(email)
                .orElseThrow(() -> new UserWithEmailNotFoundException(email)));
        ads.setImage(imageService.saveImage(image, "/ads"));
        adsRepository.save(ads);
        return adsMapper.toAdsDto(ads);
    }


    @Override
    public FullAds getAds(Integer id) {
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ads not found by id: " + id));
        return adsMapper.toFullAds(ads);
    }


    @Transactional
    @Override
    public void removeAd(Integer id) {
        commentRepository.deleteAllByAds_Id(id);
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ads not found by id: " + id));
        imageService.deleteFileIfNotNull(ads.getImage());
        log.trace("Removed Ads with id: ", id);
        adsRepository.delete(ads);
    }



    @Override
    public AdsDto updateAds(CreateAds createAds, Integer id) {
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ads not found by id: " + id));
        adsMapper.updateAds(createAds, ads);
        adsRepository.save(ads);
        log.trace("Updated Ads with id: ", id);
        return adsMapper.toAdsDto(ads);
    }


    @Override
    public ResponseWrapperComment getComments(Integer id) {
        List<Comment> commentList = commentRepository.findAllByAdsId(id);
        List<CommentDto> commentDtos = commentMapper.toListDto(commentList);
        ResponseWrapperComment responseWrapperComment = new ResponseWrapperComment();
        responseWrapperComment.setResults(commentDtos);
        responseWrapperComment.setCount(commentDtos.size());
        return responseWrapperComment;
    }


    @Override
    public CommentDto addComment(Integer id, CreateComment createComment, String email) {
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ads not found"));
        Comment comment = commentMapper.toCommentFromCreateComment(createComment);
        comment.setAds(ads);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(userRepository.findByEmail(email).get());
        commentRepository.save(comment);
        log.trace("Added comment with id: ", comment.getId());
        return commentMapper.toCommentDtoFromComment(comment);
    }



    @Override
    @Transactional
    public void deleteComment(Integer adId, Integer id) {
        commentRepository.deleteByAdsIdAndId(adId, id);
        log.trace("Deleted comment with id: ", id);
    }


    @Override
    public CommentDto updateComment(Integer adId, Integer id, CreateComment createComment) {
        Comment comment = commentRepository.findCommentByIdAndAds_Id(id, adId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));
        comment.setText(createComment.getText());
        commentRepository.save(comment);
        log.trace("Updated comment with id: ", id);
        return commentMapper.toCommentDtoFromComment(comment);
    }


    @Override
    public void updateAdsImage(Integer id, MultipartFile image) {
        Ads ads = adsRepository.findById(id)
                .orElseThrow(() -> new AdsNotFoundException("Ads not found"));
        imageService.deleteFileIfNotNull(ads.getImage());
        ads.setImage(imageService.saveImage(image, "/ads"));
        adsRepository.save(ads);
    }


    @Override
    public byte[] getImage(String name) throws IOException {
        return imageService.getImage(name);
    }


    @Override
    public CommentDto getCommentDto(Integer adId, Integer id) {
        Comment comment = commentRepository.findCommentByIdAndAds_Id(id, adId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));
        return commentMapper.toCommentDtoFromComment(comment);
    }

    public String getUserNameOfComment(Integer id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"))
                .getUser().getEmail();
    }
}
