import SwiftUI
import shared

struct HomeView: View {
    @EnvironmentObject var workoutViewModel: SharedWorkoutViewModel
    @EnvironmentObject var goalViewModel: SharedGoalViewModel
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 20) {
                    QuickStatsView()
                    
                    ActionButtonsView()
                    
                    RecentWorkoutsView()
                    
                    ActiveGoalsView()
                }
                .padding()
            }
            .navigationTitle("FitTrack Pro")
        }
    }
}

struct QuickStatsView: View {
    @EnvironmentObject var workoutViewModel: SharedWorkoutViewModel
    
    var body: some View {
        VStack {
            Text("Quick Stats")
                .font(.headline)
                .frame(maxWidth: .infinity, alignment: .leading)
            
            HStack(spacing: 20) {
                StatItemView(
                    value: "\(workoutViewModel.workouts.count)",
                    label: "Workouts",
                    systemImage: "figure.run"
                )
                
                StatItemView(
                    value: "\(totalMinutes)",
                    label: "Minutes",
                    systemImage: "clock.fill"
                )
                
                StatItemView(
                    value: "\(totalCalories)",
                    label: "Calories",
                    systemImage: "flame.fill"
                )
            }
        }
        .padding()
        .background(Color(.systemBackground))
        .cornerRadius(12)
        .shadow(radius: 2)
    }
    
    private var totalMinutes: Int {
        workoutViewModel.workouts.reduce(0) { $0 + Int($1.duration) }
    }
    
    private var totalCalories: Int {
        workoutViewModel.workouts.reduce(0) { $0 + Int($1.caloriesBurned) }
    }
}

struct StatItemView: View {
    let value: String
    let label: String
    let systemImage: String
    
    var body: some View {
        VStack {
            Image(systemName: systemImage)
                .font(.title2)
                .foregroundColor(.accentColor)
            Text(value)
                .font(.title3)
                .bold()
            Text(label)
                .font(.caption)
                .foregroundColor(.secondary)
        }
    }
}

struct ActionButtonsView: View {
    @State private var showingAddWorkout = false
    @State private var showingAddGoal = false
    
    var body: some View {
        HStack(spacing: 20) {
            Button(action: { showingAddWorkout = true }) {
                HStack {
                    Image(systemName: "plus.circle.fill")
                    Text("Add Workout")
                }
                .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .sheet(isPresented: $showingAddWorkout) {
                AddWorkoutView()
            }
            
            Button(action: { showingAddGoal = true }) {
                HStack {
                    Image(systemName: "flag.fill")
                    Text("Set Goal")
                }
                .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .sheet(isPresented: $showingAddGoal) {
                AddGoalView()
            }
        }
    }
}

struct RecentWorkoutsView: View {
    @EnvironmentObject var workoutViewModel: SharedWorkoutViewModel
    
    var body: some View {
        VStack {
            HStack {
                Text("Recent Workouts")
                    .font(.headline)
                Spacer()
                NavigationLink("See All") {
                    WorkoutsView()
                }
                .font(.subheadline)
            }
            
            if workoutViewModel.workouts.isEmpty {
                Text("No workouts yet")
                    .foregroundColor(.secondary)
                    .padding()
            } else {
                ForEach(workoutViewModel.workouts.prefix(3), id: \.id) { workout in
                    WorkoutRowView(workout: workout)
                }
            }
        }
        .padding()
        .background(Color(.systemBackground))
        .cornerRadius(12)
        .shadow(radius: 2)
    }
}

struct ActiveGoalsView: View {
    @EnvironmentObject var goalViewModel: SharedGoalViewModel
    
    var body: some View {
        VStack {
            HStack {
                Text("Active Goals")
                    .font(.headline)
                Spacer()
                NavigationLink("See All") {
                    GoalsView()
                }
                .font(.subheadline)
            }
            
            if goalViewModel.activeGoals.isEmpty {
                Text("No active goals")
                    .foregroundColor(.secondary)
                    .padding()
            } else {
                ForEach(goalViewModel.activeGoals.prefix(3), id: \.id) { goal in
                    GoalRowView(goal: goal)
                }
            }
        }
        .padding()
        .background(Color(.systemBackground))
        .cornerRadius(12)
        .shadow(radius: 2)
    }
}
