package guru.qa.niffler.test.web;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.AuthDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JdbcTest {
    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-3",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx",
                        null
                )
        );
        System.out.println(spend);
    }

    @Test
    void xaTransactionTest() {
        AuthDbClient authDbClient = new AuthDbClient();

        UserJson user = authDbClient.createUser(
                 new UserJson(
                        null,
                         RandomDataUtils.randomUsername(),
                         RandomDataUtils.randomName(),
                         RandomDataUtils.randomSurname(),
                         null,
                         CurrencyValues.RUB,
                         null,
                         null
                )
        );
        System.out.println(user);
    }

}

