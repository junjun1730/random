CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(20)  NOT NULL DEFAULT 'USER',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_users_email (email)
);

  CREATE TABLE user_credentials (
      id          BIGINT       NOT NULL AUTO_INCREMENT,
      user_id     BIGINT       NOT NULL,
      password    VARCHAR(255) NOT NULL,
      PRIMARY KEY (id),
      UNIQUE KEY uq_credentials_user (user_id),
      FOREIGN KEY (user_id) REFERENCES users(id)
  );

  CREATE TABLE user_oauth (
      id              BIGINT       NOT NULL AUTO_INCREMENT,
      user_id         BIGINT       NOT NULL,
      provider        VARCHAR(20)  NOT NULL,
      provider_id     VARCHAR(255) NOT NULL,
      PRIMARY KEY (id),
      UNIQUE KEY uq_oauth_provider (provider, provider_id),
      FOREIGN KEY (user_id) REFERENCES users(id)
  );
