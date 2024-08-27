package com.elice.showpet.article.aop;

import com.elice.showpet.article.dto.CreateArticleDto;
import com.elice.showpet.article.dto.UpdateArticleDto;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class AopUtils {
    private final StringBuilder sb = new StringBuilder();

    public String argsToString(Object[] args) {
        return String.join(", ", Arrays.stream(args)
                .filter(this::argFilter)
                .map(this::argToString)
                .toList());
    }

    private boolean argFilter(Object arg) {
        return !(arg instanceof Model || arg instanceof Errors);
    }

    private String argToString(Object arg) {
        if (arg == null) return "";
        sb.setLength(0);
        if (arg instanceof CreateArticleDto) return arg.toString();
        if (arg instanceof UpdateArticleDto) return arg.toString();
        if (arg instanceof MultipartFile file) {
            sb.append("File(content-type = ").append(file.getContentType());
            sb.append(", filename = ").append(file.getName()).append(")");
            return sb.toString();
        }
        return arg.toString();
    }
}
