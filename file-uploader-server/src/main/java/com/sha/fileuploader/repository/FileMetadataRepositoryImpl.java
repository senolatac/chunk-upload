package com.sha.fileuploader.repository;

import com.sha.fileuploader.model.FileMetadata;
import com.sha.fileuploader.utils.DaoUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;

@Transactional
@Repository
public class FileMetadataRepositoryImpl extends AbstractGenericDao<FileMetadata> implements FileMetadataRepository {

    @Override
    public FileMetadata findByHash(String hash){
        String hql = "Select f from FileMetadata f where f.hash = :pHash ";
        Query query = em.createQuery(hql);
        query.setParameter("pHash", hash);
        return (FileMetadata) DaoUtil.firstOrNull(query.getResultList());
    }

    @Override
    public FileMetadata findByUniqueId(String uniqueId){
        String hql = "Select f from FileMetadata f where f.uniqueId = :pUniqueId ";
        Query query = em.createQuery(hql);
        query.setParameter("pUniqueId", uniqueId);
        return (FileMetadata) DaoUtil.firstOrNull(query.getResultList());
    }
}
