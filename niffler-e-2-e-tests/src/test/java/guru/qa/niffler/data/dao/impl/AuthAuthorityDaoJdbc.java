package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.authUser.AuthUserEntity;
import guru.qa.niffler.data.entity.authUser.Authority;
import guru.qa.niffler.data.entity.authUser.AuthorityEntity;

import java.sql.*;
import java.util.*;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthorityEntity create(AuthorityEntity authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO authority (user_id, authority) " +
                        "VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, authority.getUser().getId());
            ps.setString(2, authority.getAuthority().name());
            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
                authority.setId(generatedKey);
                return authority;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findAuthorityByUserId(UUID id) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM \"authority\" WHERE user_id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                AuthUserDaoJdbc authUserDaoJdbc = new AuthUserDaoJdbc(connection);
                List<AuthorityEntity> categoryList = new ArrayList<>();
                while (rs.next()) {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setId(rs.getObject("id", UUID.class));
                    Optional<AuthUserEntity> authUserEntity = authUserDaoJdbc.findById(rs.getObject("user_id",
                            UUID.class));
                    if (authUserEntity.isEmpty()) {
                        throw new NoSuchElementException("Can't find user");
                    } else {
                        ae.setUser(authUserEntity.get());
                    }
                    ae.setAuthority(Authority.valueOf(rs.getString("authority")));
                }
                return categoryList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthorityEntity updateAuthority(AuthorityEntity authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE authority SET (authority) = (?) WHERE id = ?"
        )) {
            ps.setString(2, authority.getAuthority().name());
            ps.setObject(2, authority.getId());
            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                throw new NoSuchElementException("Can`t find user authority to update");
            } else {
                return authority;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthorityEntity authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM authority WHERE id = ?"
        )) {
            ps.setObject(1, authority.getId());
            int updatedRows = ps.executeUpdate();
            if (updatedRows == 0) {
                throw new NoSuchElementException("Can`t find authority to delete");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}