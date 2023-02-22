package com.bobocode.fp;

import com.bobocode.fp.exception.EntityNotFoundException;
import com.bobocode.model.Account;
import com.bobocode.model.Sex;
import com.bobocode.util.ExerciseNotCompletedException;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * {@link CrazyStreams} is an exercise class. Each method represent some operation with a collection of accounts that
 * should be implemented using Stream API. Every method that is not implemented yet throws
 * {@link ExerciseNotCompletedException}.
 * <p>
 * TODO: remove exception throwing and implement each method using Stream API
 * <p><p>
 * <strong>TODO: to get the most out of your learning, <a href="https://www.bobocode.com/learn">visit our website</a></strong>
 * <p>
 *
 * @author Taras Boychuk
 */
@AllArgsConstructor
public class CrazyStreams {
    private Collection<Account> accounts;

    /**
     * Returns {@link Optional} that contains an {@link Account} with the max value of balance
     *
     * @return account with max balance wrapped with optional
     */
    public Optional<Account> findRichestPerson() {

        Account richestPerson = Collections.max(accounts, Comparator.comparing(c -> c.getBalance()));
        Optional<Account> res=Optional.of(richestPerson);

        return res;

        //throw new ExerciseNotCompletedException();
    }

    /**
     * Returns a {@link List} of {@link Account} that have a birthday month equal to provided.
     *
     * @param birthdayMonth a month of birth
     * @return a list of accounts
     */
    public List<Account> findAccountsByBirthdayMonth(Month birthdayMonth) {

       return accounts.stream().filter(acc-> acc.getBirthday().getMonth().equals(birthdayMonth)).toList();

        //throw new ExerciseNotCompletedException();
    }

    /**
     * Returns a map that separates all accounts into two lists - male and female. Map has two keys {@code true} indicates
     * male list, and {@code false} indicates female list.
     *
     * @return a map where key is true or false, and value is list of male, and female accounts
     */
    public Map<Boolean, List<Account>> partitionMaleAccounts() {

       List<Account> male= accounts.stream().filter(acc->acc.getSex()== Sex.MALE).toList();
       List<Account> female= accounts.stream().filter(acc->acc.getSex()== Sex.FEMALE).toList();

        Map<Boolean, List<Account>>  map=new HashMap<>() ;
        map.put(true,male);
        map.put(false,female);

        return map;

        //throw new ExerciseNotCompletedException();
    }

    /**
     * Returns a {@link Map} that stores accounts grouped by its email domain. A map key is {@link String} which is an
     * email domain like "gmail.com". And the value is a {@link List} of {@link Account} objects with a specific email domain.
     *
     * @return a map where key is an email domain and value is a list of all account with such email
     */

   // TO DO
    public Map<String, List<Account>> groupAccountsByEmailDomain() {


        Map<String, List<Account>> res= accounts.stream().collect(Collectors.groupingBy(acc -> acc.getEmail().split("@")[1]));

        return res;

    }

    /**
     * Returns a number of letters in all first and last names.
     *
     * @return total number of letters of first and last names of all accounts
     */
    public int getNumOfLettersInFirstAndLastNames() {

       int totalNumber= accounts.stream().map(x ->x.getLastName().length() + x.getFirstName().length())
                .collect(Collectors.summingInt(Integer::intValue));
       return totalNumber;
       //throw new ExerciseNotCompletedException();
    }

    /**
     * Returns a total balance of all accounts.
     *
     * @return total balance of all accounts
     */
    public BigDecimal calculateTotalBalance() {

        BigDecimal sum=accounts.stream().map(a->a.getBalance()).reduce(BigDecimal.ZERO,BigDecimal::add);
        return sum;
        //throw new ExerciseNotCompletedException();
    }

    /**
     * Returns a {@link List} of {@link Account} objects sorted by first and last names.
     *
     * @return list of accounts sorted by first and last names
     */
    public List<Account> sortByFirstAndLastNames() {
        Comparator<Account> compareByFirstName = Comparator.comparing( Account::getFirstName );
        Comparator<Account> compareByLastName = Comparator.comparing( Account::getLastName );
        List<Account> sorted= accounts.stream().sorted(compareByFirstName.thenComparing(compareByLastName)).toList();

        return sorted;

        //throw new ExerciseNotCompletedException();
    }

    /**
     * Checks if there is at least one account with provided email domain.
     *
     * @param emailDomain
     * @return true if there is an account that has an email with provided domain
     */
    public boolean containsAccountWithEmailDomain(String emailDomain) {


      List<Account> acc=  accounts.stream().filter(e->e.getEmail().contains(emailDomain)).toList() ;
      return acc.size()>0?true:false;

        //throw new ExerciseNotCompletedException();
    }

    /**
     * Returns account balance by its email. Throws @link EntityNotFoundException with message
     * "Cannot find Account by email={email}" if account is not found.
     *
     * @param email account email
     * @return account balance
     */



    public BigDecimal getBalanceByEmail(String email) {

        Account account= accounts.stream().filter(acc ->acc.getEmail().contains(email)).findAny().orElse(null);
        if(account==null){

            throw new EntityNotFoundException("Cannot find Account by email="+email);
        }
        else{
            return account.getBalance();
        }

    }

    /**
     * Collects all existing accounts into a {@link Map} where a key is account id, and the value is {@link Account} instance
     *
     * @return map of accounts by its ids
     */
    public Map<Long, Account> collectAccountsById() {


    Map<Account,Long> res=  accounts.stream().collect(  Collectors.groupingBy(acc -> acc,  Collectors.summingLong( Account::getId) ) );


        return res.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        // throw new ExerciseNotCompletedException();
    }

    /**
     * Filters accounts by the year when an account was created. Collects account balances by its emails into a {@link Map}.
     * The key is @link Account#email and the value is @link Account#balance
     *
     * @param year the year of account creation
     * @return map of account by its ids the were created in a particular year
     */
    public Map<String, BigDecimal> collectBalancesByEmailForAccountsCreatedOn(int year) {


        Map<String, BigDecimal> collection= accounts.stream().filter(acc->acc.getCreationDate().getYear()==year).collect(Collectors.toMap(a ->a.getEmail(),t->t.getBalance()));

        return collection;
        //throw new ExerciseNotCompletedException();
    }

    /**
     * Returns a {@link Map} where key is @link Account#lastName and values is a {@link Set} that contains first names
     * of all accounts with a specific last name.
     *
     * @return a map where key is a last name and value is a set of first names
     */
    public Map<String, Set<String>> groupFirstNamesByLastNames() {

       //accounts.stream().collect(Collectors.groupingBy(Account::getLastName, Collectors.groupingBy(Account::getFirstName,toSet())));

        Map<String, Set<String>> res=  accounts.stream().collect(Collectors.groupingBy(Account::getLastName ,Collectors.mapping(Account::getFirstName, Collectors.toSet())));
       return res;
    }

    /**
     * Returns a {@link Map} where key is a birthday month, and value is a {@link String} that stores comma and space
     * -separated first names (e.g. "Polly, Dylan, Clark"), of all accounts that have the same birthday month.
     *
     * @return a map where a key is a birthday month and value is comma-separated first names
     */


    public Map<Month, String> groupCommaSeparatedFirstNamesByBirthdayMonth() {

    Map<Month, String> res=  accounts.stream().collect(Collectors.groupingBy(acc->acc.getBirthday().getMonth(), Collectors.mapping(Account::getFirstName, Collectors.joining(", ")) ));

      return res;
    }

    /**
     * Returns a {@link Map} where key is a {@link Month} of @link Account#creationDate, and value is total balance
     * of all accounts that have the same value creation month.
     *
     * @return a map where key is a creation month and value is total balance of all accounts created in that month
     */



    public Map<Month, BigDecimal> groupTotalBalanceByCreationMonth() {


        Map<Month, BigDecimal>  res=    accounts.stream().collect(Collectors.groupingBy( acc->acc.getCreationDate().getMonth()  , Collectors.reducing(BigDecimal.ZERO, Account::getBalance, BigDecimal::add)  ));

        return res;
        //throw new ExerciseNotCompletedException();
    }

    /**
     * Returns a {@link Map} where key is a letter {@link Character}, and value is a number of its occurrences in
     * @link Account#firstName.
     *
     * @return a map where key is a letter and value is its count in all first names
     */

    public Map<Character, Long> getCharacterFrequencyInFirstNames() {

        return accounts.stream().flatMap(item->Arrays.stream(item.getFirstName().split("")))
                .collect(groupingBy(c->c.charAt(0),Collectors.counting()));

//        accounts.stream().map(item->item.getFirstName().split("")).
//        map(array->array.toString())

    //ab=accounts.stream().map(el ->  Arrays.stream(el.getFirstName().split("")).collect(Collectors.groupingBy(a->a, Collectors.counting())));


    }



    /**
     * Returns a {@link Map} where key is a letter {@link Character}, and value is a number of its occurrences ignoring
     * case, in all @link Account#firstName and @link Account#lastName that are equal or longer than nameLengthBound.
     * Inside the map, all letters should be stored in lower case.
     *
     * @return a map where key is a letter and value is its count ignoring case in all first and last names
     */

    public Map<Character, Long> getCharacterFrequencyIgnoreCaseInFirstAndLastNames(int nameLengthBound) {



       return accounts.stream().flatMap(account -> Stream.of(account.getLastName(), account.getFirstName()))
                .filter(fullName -> fullName.length() >= nameLengthBound)
                .flatMapToInt(name -> name.toLowerCase().chars())
                .mapToObj(ch -> (char) ch)
                .collect(Collectors.groupingBy(ch -> ch, Collectors.counting()
                ));




    }

}

