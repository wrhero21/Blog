package com.lrm.blog.service;

import com.lrm.blog.NotFoundException;
import com.lrm.blog.dao.TagsRepository;
import com.lrm.blog.po.Tag;
import com.lrm.blog.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagsRepository tagsRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagsRepository.save(tag);
    }
    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagsRepository.findOne(id);
    }
    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagsRepository.findByName(name);
    }
    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagsRepository.findOne(id);
        if (t == null){
            throw new NotFoundException("不存在該標籤");
        }
        BeanUtils.copyProperties(tag,t);
        return tagsRepository.save(t);
    }
    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagsRepository.delete(id);

    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagsRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagsRepository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagsRepository.findAll(converToList(ids));
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = new PageRequest(0,size,sort);
        return tagsRepository.findTop(pageable);
    }

    private List<Long> converToList(String ids) {
        List<Long> list = new ArrayList<>();
        if(!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i = 0; i < idarray.length ; i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
}
