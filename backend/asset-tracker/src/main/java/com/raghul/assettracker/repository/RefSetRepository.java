package com.raghul.assettracker.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raghul.assettracker.model.RefSetModel;

public interface RefSetRepository extends JpaRepository<RefSetModel, UUID> {

	RefSetModel findByRefSetKey(String refSet);

}
