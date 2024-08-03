package transaction;

import java.util.HashMap;
import java.util.Map;

public class TransactionAuthorizer {
    private static final Map<String, Category> mccToCategoryMap = new HashMap<>();
    private static final Map<String, Category> merchantToCategoryMap = new HashMap<>();

    static {
        // Mapeamento MCC para categorias
        mccToCategoryMap.put("5411", Category.FOOD);
        mccToCategoryMap.put("5412", Category.FOOD);
        mccToCategoryMap.put("5811", Category.MEAL);
        mccToCategoryMap.put("5812", Category.MEAL);

        // Mapeamento Merchant para categorias
        merchantToCategoryMap.put("UBER EATS                   SAO PAULO BR", Category.MEAL);
        merchantToCategoryMap.put("PAG*JoseDaSilva          RIO DE JANEI BR", Category.CASH);
    }

    private final Balance balance = new Balance();

    public String authorizeTransaction(Transaction transaction) {
        Category category = determineCategory(transaction);

        if (category == null) {
            category = Category.CASH; // fallback para CASH
        }

        return processTransaction(transaction.getAmount(), category);
    }

    private Category determineCategory(Transaction transaction) {
        // Verificar a categoria baseada no nome do comerciante
        Category category = merchantToCategoryMap.get(transaction.getMerchant());
        if (category != null) {
            return category;
        }

        // Verificar a categoria baseada no MCC
        return mccToCategoryMap.get(transaction.getMcc());
    }

    private String processTransaction(double amount, Category category) {
        // Validar e processar a transação baseada no saldo da categoria
        switch (category) {
            case FOOD:
                if (balance.getFood() >= amount) {
                    balance.setFood(balance.getFood() - amount);
                    return "{\"code\": \"00\"}"; // Aprovada
                }
                break;
            case MEAL:
                if (balance.getMeal() >= amount) {
                    balance.setMeal(balance.getMeal() - amount);
                    return "{\"code\": \"00\"}"; // Aprovada
                }
                break;
            case CASH:
                if (balance.getCash() >= amount) {
                    balance.setCash(balance.getCash() - amount);
                    return "{\"code\": \"00\"}"; // Aprovada
                }
                break;
            default:
                return "{\"code\": \"07\"}"; // Outro problema
        }
        return "{\"code\": \"51\"}"; // Rejeitada, saldo insuficiente
    }
}
