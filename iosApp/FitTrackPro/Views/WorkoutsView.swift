import SwiftUI
import shared

struct WorkoutsView: View {
    @EnvironmentObject var viewModel: SharedWorkoutViewModel
    @State private var showingAddWorkout = false
    @State private var searchText = ""
    
    var filteredWorkouts: [Workout] {
        if searchText.isEmpty {
            return viewModel.workouts
        } else {
            return viewModel.workouts.filter { workout in
                workout.name.localizedCaseInsensitiveContains(searchText) ||
                workout.type.localizedCaseInsensitiveContains(searchText)
            }
        }
    }
    
    var body: some View {
        NavigationView {
            List {
                ForEach(filteredWorkouts, id: \.id) { workout in
                    WorkoutRowView(workout: workout)
                }
                .onDelete { indexSet in
                    deleteWorkouts(at: indexSet)
                }
            }
            .searchable(text: $searchText)
            .refreshable {
                Task {
                    await viewModel.loadWorkouts()
                }
            }
            .navigationTitle("Workouts")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: { showingAddWorkout = true }) {
                        Image(systemName: "plus")
                    }
                }
            }
            .sheet(isPresented: $showingAddWorkout) {
                AddWorkoutView()
            }
        }
    }
    
    private func deleteWorkouts(at offsets: IndexSet) {
        for index in offsets {
            let workout = filteredWorkouts[index]
            Task {
                await viewModel.deleteWorkout(id: workout.id)
            }
        }
    }
}

struct WorkoutRowView: View {
    let workout: Workout
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                Text(workout.name)
                    .font(.headline)
                Spacer()
                Text(workout.type)
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            }
            
            HStack {
                Label("\(workout.duration) min", systemImage: "clock")
                Spacer()
                Label("\(workout.caloriesBurned) cal", systemImage: "flame.fill")
            }
            .font(.subheadline)
            .foregroundColor(.secondary)
            
            if !workout.notes.isEmpty {
                Text(workout.notes)
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .lineLimit(2)
            }
        }
        .padding(.vertical, 8)
    }
}

struct AddWorkoutView: View {
    @Environment(\.dismiss) var dismiss
    @EnvironmentObject var viewModel: SharedWorkoutViewModel
    
    @State private var name = ""
    @State private var type = ""
    @State private var duration: Double = 30
    @State private var caloriesBurned: Double = 100
    @State private var notes = ""
    
    let workoutTypes = ["Running", "Walking", "Cycling", "Swimming", "Strength", "Yoga", "Other"]
    
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Workout Details")) {
                    TextField("Name", text: $name)
                    
                    Picker("Type", selection: $type) {
                        ForEach(workoutTypes, id: \.self) { type in
                            Text(type).tag(type)
                        }
                    }
                    
                    HStack {
                        Text("Duration")
                        Spacer()
                        TextField("Duration", value: $duration, format: .number)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                        Text("min")
                    }
                    
                    HStack {
                        Text("Calories")
                        Spacer()
                        TextField("Calories", value: $caloriesBurned, format: .number)
                            .keyboardType(.numberPad)
                            .multilineTextAlignment(.trailing)
                        Text("cal")
                    }
                }
                
                Section(header: Text("Notes")) {
                    TextEditor(text: $notes)
                        .frame(height: 100)
                }
            }
            .navigationTitle("Add Workout")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") {
                        dismiss()
                    }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Save") {
                        saveWorkout()
                    }
                    .disabled(name.isEmpty || type.isEmpty)
                }
            }
        }
    }
    
    private func saveWorkout() {
        let workout = Workout(
            id: UUID().uuidString,
            name: name,
            type: type,
            duration: Int32(duration),
            caloriesBurned: Int32(caloriesBurned),
            notes: notes,
            timestamp: Int64(Date().timeIntervalSince1970)
        )
        
        Task {
            await viewModel.addWorkout(workout)
            dismiss()
        }
    }
}
