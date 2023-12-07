package com.example.marketplace.web;


import com.example.marketplace.model.AppUserDetails;
import com.example.marketplace.model.dtos.CommentDTO;
import com.example.marketplace.service.CommentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {


    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public String create(CommentDTO commentDTO, @AuthenticationPrincipal AppUserDetails userDetails) {
        commentService.create(commentDTO, userDetails);

        return "redirect:/products/details/" + commentDTO.getProductId();
    }

    @DeleteMapping("/{id}/{routeId}")
    public String delete(@PathVariable("id") Long id, @PathVariable("routeId") Long routeId) {
        commentService.delete(id);

        return "redirect:/products/details/" + routeId;
    }
}
