package housit.housit_backend.domain.finance;

import jakarta.persistence.*;
import housit.housit_backend.domain.room.Room;

import java.util.Date;
import java.util.List;

@Entity
public class FinancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long financePlanId;

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @Temporal(TemporalType.DATE)
    private Date month;

    @OneToMany(mappedBy = "financePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PredictedIncome> predictedIncomes;

    @OneToMany(mappedBy = "financePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavingGoal> savingGoals;

    @OneToMany(mappedBy = "financePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PredictedExpense> predictedExpenses;

    // Getters and Setters
}