import SwiftUI
import shared

struct GoalsView: View {
    @EnvironmentObject var viewModel: SharedGoalViewModel
    @State private var showingAddGoal = false
    
    var body: some View {
        NavigationView {
            List {
                Section(header: Text("Active Goals")) {
                    ForEach(viewModel.activeGoals, id: \.id) { goal in
                        GoalRowView(goal: goal)
                    }
                }
                
                Section(header: Text("Completed Goals")) {
                    ForEach(viewModel.goals.filter { $0.completed }, id: \.id) { goal in
                        GoalRowView(goal: goal)
                    }
                }
            }
            .refreshable {
                Task {
                    await viewModel.loadGoals()
                }
            }
            .navigationTitle("Goals")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { showingAddGoal = true }) {
                        Image(systemName: "plus")
                    }
                }
            }
            .sheet(isPresented: $showingAddGoal) {
                AddGoalView()
            }
        }
    }
}

struct GoalRowView: View {
    @EnvironmentObject var viewModel: SharedGoalViewModel
    let goal: Goal
    
    var progress: Double {
        Double(goal.progress) / Double(goal.target)
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                Text(goal.name)
                    .font(.headline)
                Spacer()
                Text(goal.type)
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            }
            
            ProgressView(value: progress) {
                HStack {
                    Text("\(goal.progress) / \(goal.target)")
                    Spacer()
                    Text(goal.completed ? "Completed" : "\(Int(progress * 100))%")
                }
                .font(.caption)
                .foregroundColor(.secondary)
            }
            
            if !goal.notes.isEmpty {
                Text(goal.notes)
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .lineLimit(2)
            }
        }
        .padding(.vertical, 8)
        .swipeActions(edge: .trailing) {
            if !goal.completed {
                Button {
                    updateProgress()
                } label: {
                    Label("Progress", systemImage: "plus.circle")
                }
                .tint(.blue)
            }
        }
    }
    
    private func updateProgress() {
        let newProgress = goal.progress + 1
        let completed = newProgress >= goal.target
        
        Task {
            await viewModel.updateGoalProgress(
                id: goal.id,
                progress: newProgress,
                completed: completed
            )
        }
    }
}

struct AddGoalView: View {
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject var viewModel: GoalViewModel
    
    @State private var name = ""
    @State private var type = ""
    @State private var target: Double = 1
    @State private var notes = ""
    
    let goalTypes = ["Steps", "Workouts", "Minutes", "Calories", "Distance", "Custom"]
    
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Goal Details")) {
                    TextField("Name", text: $name)
                    
                    Picker("Type", selection: $type) {
                        ForEach(goalTypes, id: \.self) { type in
                            Text(type).tag(type)
                        }
                    }
                    
                    HStack {
                        Text("Target")
                        Spacer()
                        TextField("Target", value: $target, format: .number)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                    }
                }
                
                Section(header: Text("Notes")) {
                    TextEditor(text: $notes)
                        .frame(height: 100)
                }
            }
            .navigationTitle("Add Goal")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") {
                        dismiss()
                    }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Save") {
                        saveGoal()
                    }
                    .disabled(name.isEmpty || type.isEmpty || target < 1)
                }
            }
        }
    }
    
    private func saveGoal() {
        let goal = Goal(
            id: UUID().uuidString,
            name: name,
            type: type,
            target: Int32(target),
            progress: 0,
            completed: false,
            notes: notes,
            timestamp: Int64(Date().timeIntervalSince1970)
        )
        
        Task {
            await viewModel.addGoal(goal)
            dismiss()
        }
    }
}
