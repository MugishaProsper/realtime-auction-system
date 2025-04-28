package com.webapp.realtimeauctionbackend.repositories;

import com.webapp.realtimeauctionbackend.constants.TransactionStatus;
import com.webapp.realtimeauctionbackend.models.Transaction;
import com.webapp.realtimeauctionbackend.models.Wallet;
import com.webapp.realtimeauctionbackend.models.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.fromWallet.user.id = :userId OR t.toWallet.user.id = :userId")
    List<Transaction> findByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t WHERE (t.fromWallet.user.id = :userId OR t.toWallet.user.id = :userId) AND t.status = :status")
    List<Transaction> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") TransactionStatus status);

    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN :start AND :end")
    List<Transaction> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT t FROM Transaction t WHERE t.fromWallet.id = :walletId OR t.toWallet.id = :walletId")
    List<Transaction> findByWalletId(@Param("walletId") Long walletId);

    List<Transaction> findByWallet(Wallet wallet);
    List<Transaction> findByWalletAndTimestampBetween(Wallet wallet, LocalDateTime start, LocalDateTime end);
    List<Transaction> findByWalletAndType(Wallet wallet, TransactionType type);
    List<Transaction> findByWalletAndStatus(Wallet wallet, TransactionStatus status);

    Page<Transaction> findByWallet(Wallet wallet, Pageable pageable);
    
    Page<Transaction> findByWalletAndTimestampBetween(Wallet wallet, LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    Page<Transaction> findByWalletAndType(Wallet wallet, TransactionType type, Pageable pageable);
    
    Page<Transaction> findByWalletAndStatus(Wallet wallet, TransactionStatus status, Pageable pageable);
    
    @Query("SELECT t FROM Transaction t WHERE t.wallet = :wallet AND t.timestamp >= :since ORDER BY t.timestamp DESC")
    List<Transaction> findRecentTransactions(@Param("wallet") Wallet wallet, @Param("since") LocalDateTime since);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.wallet = :wallet AND t.type = :type AND t.status = 'COMPLETED'")
    Double getTotalAmountByType(@Param("wallet") Wallet wallet, @Param("type") TransactionType type);
} 